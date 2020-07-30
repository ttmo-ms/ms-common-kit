/*
 * Copyright (C) 2006 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ttmo.ms.common.kit.dynamic.query.aspect;

import com.ttmo.ms.common.kit.dynamic.query.annotation.DynamicQuery;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

/**
 * Some of this class modified from {@code com.google.guava} (Apache license 2.0).
 * {@link CaseFormat}
 * {@link CharMatcher}
 *
 * @author Jover Zhang
 */
@Aspect
public class DynamicQueryAspect {

    /**
     * For all {@code customQuery} in {@link com.ttmo.ms.common.kit.dynamic.query.annotation.DynamicQuery}.
     * That in before calling, filter name of variable equals @DynamicQuery({@code value}).
     * Convert "lowerCamel" to "lower_underscore"(The "MySql" variable naming convention).
     */
    @Around("@annotation(com.ttmo.ms.common.kit.dynamic.query.annotation.DynamicQuery)")
    public Object beforeEnhancement(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();

        String valueName = method.getAnnotation(DynamicQuery.class).value();

        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getName().equals(valueName)) {
                String arg;

                if (args[i].getClass().isArray()) {
                    arg = Strings.join(Arrays.asList((String[]) args[i]), ',');
                } else {
                    arg = args[i].toString();
                }

                args[i] = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, arg);
                break;
            }
        }

        return pjp.proceed(args);
    }

    /**
     * Provider "lowerCamel" to "lower_underscore" conversion.
     */
    private enum CaseFormat {

        /**
         * C++ variable naming convention, e.g., "lower_underscore".
         */
        LOWER_UNDERSCORE(CharMatcher.is('_'), "_") {
            @Override
            String normalizeWord(String word) {
                return word.toLowerCase();
            }
        },

        /**
         * Java variable naming convention, e.g., "lowerCamel".
         */
        LOWER_CAMEL(CharMatcher.isRange('A', 'Z'), "") {
            @Override
            String normalizeWord(String word) {
                return firstCharOnlyToUpper(word);
            }

            @Override
            String normalizeFirstWord(String word) {
                return word.toLowerCase();
            }
        };

        /**
         * @see CharMatcher
         */
        private final CharMatcher wordBoundary;

        private final String workSeparator;

        CaseFormat(CharMatcher wordBoundary, String wordSeparator) {
            this.wordBoundary = wordBoundary;
            this.workSeparator = wordSeparator;
        }

        public String to(CaseFormat format, String str) {
            return (format == this) ? str : convert(format, str);
        }

        private String convert(CaseFormat format, String s) {
            StringBuilder out = null;
            int i = 0,
                j = -1;
            while ((j = wordBoundary.indexIn(s, ++j)) != -1) {
                if (i == 0) {
                    out = new StringBuilder(s.length() + 4 * workSeparator.length());
                    out.append(format.normalizeFirstWord(s.substring(i, j)));
                } else {
                    out.append(format.normalizeWord(s.substring(i, j)));
                }
                out.append(format.workSeparator);
                i = j + workSeparator.length();
            }
            return (i == 0)
                ? format.normalizeFirstWord(s)
                : out.append(format.normalizeWord(s.substring(i))).toString();
        }

        abstract String normalizeWord(String word);

        String normalizeFirstWord(String word) {
            return normalizeWord(word);
        }

        private static String firstCharOnlyToUpper(String word) {
            return word.isEmpty()
                ? word
                : word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
        }
    }

    /**
     * Helper {@link CaseFormat} matches words.
     */
    private static class CharMatcher {

        private CharMatcher() {
        }

        public boolean matches(char c) {
            return false;
        }

        public static CharMatcher is(final char match) {
            return new CharMatcher() {
                @Override
                public boolean matches(char c) {
                    return c == match;
                }
            };
        }

        public static CharMatcher isRange(final char startInclusive, final char endInclusive) {
            return new CharMatcher() {
                @Override
                public boolean matches(char c) {
                    return startInclusive <= c && c <= endInclusive;
                }
            };
        }

        public int indexIn(String str, int start) {
            for (int i = start; i < str.length(); i++) {
                if (matches(str.charAt(i))) {
                    return i;
                }
            }
            return -1;
        }

    }

}
