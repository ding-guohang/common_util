package com.ca.log.monitor;

import com.google.common.collect.Maps;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Monitor
 *
 * @author guohang.ding on 16-11-22
 * @see com.ca.log.web.DMonitor.jsp
 */
@SuppressWarnings("all")
public class DMonitor {

    private static Map<String, DMonitor.MonitorItem> items = new ConcurrentHashMap<>();
    private static Map<String, AtomicLong> values = new ConcurrentHashMap<>();
    private static Map<String, DMonitor.MonitorItem> jvmItems = new ConcurrentHashMap<>();
    private static Map<String, Long> currentItems = Maps.newHashMap();
    private static Timer timer = new Timer("DMonitor", true);
    private static long lastUpdate;

    public DMonitor() {
    }

    public static void recordOne(String name, long time) {
        recordMany(name, 1L, time);
    }

    public static void recordOne(String name) {
        recordMany(name, 1L, 0L);
    }

    public static void decrRecord(String name) {
        recordMany(name, -1L, 0L);
    }

    public static void recordMany(String name, long count, long time) {
        DMonitor.MonitorItem item = items.get(name);
        if (item == null) {
            item = new DMonitor.MonitorItem();
            items.put(name, item);
        }

        item.add(count, time);
    }

    //注意: 我并不会清空它的值!
    public static void recordSize(String name, long size) {
        AtomicLong v = values.get(name);
        if (v == null) {
            v = new AtomicLong();
            values.put(name, v);
        }

        v.set(size);
    }

    public static void recordValue(String name, long count) {
        AtomicLong v = values.get(name);
        if (v == null) {
            v = new AtomicLong();
            values.put(name, v);
        }

        v.addAndGet(count);
    }

    public static Map<String, Long> getValues() {
        return currentItems;
    }

    static {
        timer.schedule(new DMonitor.MonitorTask(), 0L, 2000L);
        lastUpdate = 0L;
    }

    private static class MonitorTask extends TimerTask {
        private MonitorTask() {
        }

        public void run() {
            try {
                long e = System.currentTimeMillis();
                if (e - DMonitor.lastUpdate < 50000L) {
                    return;
                }

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(e);
                if (cal.get(13) > 10) {
                    return;
                }

                DMonitor.lastUpdate = e;
                HashMap<String, Long> ret = Maps.newHashMap();
                ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
                ret.put("JVM_Thread_Count", (long) threadBean.getThreadCount());
                List beans = ManagementFactory.getGarbageCollectorMXBeans();
                Iterator i$ = beans.iterator();

                String name;
                while (i$.hasNext()) {
                    GarbageCollectorMXBean entry = (GarbageCollectorMXBean) i$.next();
                    name = "JVM_" + entry.getName();
                    long item = entry.getCollectionCount();
                    long time = entry.getCollectionTime();
                    DMonitor.MonitorItem item1 = DMonitor.jvmItems.get(name);
                    if (item1 == null) {
                        item1 = new DMonitor.MonitorItem();
                        item1.add(item, time);
                        DMonitor.jvmItems.put(name, item1);
                    }

                    ret.put(this.makeName(name + "_Count"), item - item1.count);
                    if (item - item1.count > 0L) {
                        ret.put(this.makeName(name + "_Time"), (time - item1.time) / (item - item1.count));
                    }

                    item1 = new DMonitor.MonitorItem();
                    item1.add(item, time);
                    DMonitor.jvmItems.put(name, item1);
                }

                i$ = DMonitor.items.entrySet().iterator();

                Map.Entry entry1;
                while (i$.hasNext()) {
                    entry1 = (Map.Entry) i$.next();
                    name = (String) entry1.getKey();
                    DMonitor.MonitorItem item2 = ((DMonitor.MonitorItem) entry1.getValue()).dumpAndClearItem();
                    long count = item2.count;
                    long time1 = item2.time;
                    ret.put(this.makeName(name + "_Count"), count);
                    if (count > 0L) {
                        ret.put(this.makeName(name + "_Time"), time1 / count);
                    } else {
                        ret.put(this.makeName(name + "_Time"), 0L);
                    }
                }

                i$ = DMonitor.values.entrySet().iterator();

                while (i$.hasNext()) {
                    entry1 = (Map.Entry) i$.next();
                    ret.put(this.makeName(entry1.getKey() + "_Value"), ((AtomicLong) entry1.getValue()).get());
                }

                DMonitor.currentItems = Collections.unmodifiableMap(ret);
            } catch (Exception var15) {
                //ignore
            }

        }

        private String makeName(String name) {
            return name.replaceAll(" ", "_");
        }
    }

    private static class MonitorItem {
        private long count;
        private long time;

        private MonitorItem() {
        }

        synchronized void add(long count, long time) {
            this.count += count;
            this.time += time;
        }

        synchronized DMonitor.MonitorItem dumpAndClearItem() {
            DMonitor.MonitorItem item = new DMonitor.MonitorItem();
            item.count = this.count;
            item.time = this.time;
            this.count = 0L;
            this.time = 0L;
            return item;
        }
    }
}
