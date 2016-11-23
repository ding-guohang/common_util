package com.ca.documentation.parser;

import java.lang.annotation.Annotation;

/**
 * Annotation parser for documentation
 *
 * @author guohang.ding on 16-10-11
 */
public interface AnnotationParser {

    String parse(Annotation annotation);
}
