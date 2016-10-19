package com.qunar.guohang.processor;

import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author guohang.ding on 16-10-18
 */
@SupportedAnnotationTypes({"com.qunar.guohang.processor.Demo"})
public class DemoProcessor extends AbstractProcessor {

    private final static Logger logger = LoggerFactory.getLogger(DemoProcessor.class);

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        logger.info("annotations={}", JsonUtil.encode(annotations));
        logger.info("roundEnv={}", JsonUtil.encode(roundEnv));

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Demo.class);
        logger.info("elements={}", JsonUtil.encode(elements));
        String str = "annotations=" + JsonUtil.encode(annotations) + "\n"
                + "roundEnv=" + JsonUtil.encode(roundEnv) + "\n"
                + "elements=" + JsonUtil.encode(elements);

        File file = new File("logs/console.log");
        try {
            Files.write(str.getBytes("utf-8"), file);
        } catch (IOException e) {
            //ignore
        }

        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, str);

        return false;
    }

    public static void main(String[] args) {
        File file = new File("logs/console.log");
        try {
            Files.write("aaa".getBytes("utf-8"), file);
        } catch (IOException e) {
            //ignore
        }
    }
}
