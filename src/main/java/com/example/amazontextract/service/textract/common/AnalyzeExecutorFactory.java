package com.example.amazontextract.service.textract.common;

import com.example.amazontextract.service.textract.edemsa.EdemsaAnalyzeExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class AnalyzeExecutorFactory {

    public static final Logger log = LoggerFactory.getLogger(AnalyzeExecutorFactory.class);
    private static final String EDEMSA = "edemsa";

    private final ApplicationContext context;

    public AnalyzeExecutorFactory(ApplicationContext context) {
        this.context = context;
    }

    public AnalyzeExecutor getInstance(String template) {
        String moduleClass = getClassNameFromSender(template);
        try {
            return (AnalyzeExecutor) context.getBean(Class.forName(moduleClass));
        } catch (ClassNotFoundException e) {
            log.error("Module {} was not found.", moduleClass, e);
            return null;
        }
    }

    private String getClassNameFromSender(String template) {
        if (EDEMSA.equals(template)) {
            return EdemsaAnalyzeExecutor.class.getName();
        }
        return null;
    }
}
