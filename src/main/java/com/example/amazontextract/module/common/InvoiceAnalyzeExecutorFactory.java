package com.example.amazontextract.module.common;

import com.example.amazontextract.module.edemsa.EdemsaAnalyzeExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class InvoiceAnalyzeExecutorFactory {

    public static final Logger log = LoggerFactory.getLogger(InvoiceAnalyzeExecutorFactory.class);

    private final ApplicationContext context;

    public InvoiceAnalyzeExecutorFactory(ApplicationContext context) {
        this.context = context;
    }

    public InvoiceAnalyzeExecutor getInstance(String template) {
        String moduleClass = getClassNameFromSender(template);
        try {
            return (InvoiceAnalyzeExecutor) context.getBean(Class.forName(moduleClass));
        } catch (ClassNotFoundException e) {
            log.error("Module {} was not found.", moduleClass, e);
            return null;
        }
    }

    private String getClassNameFromSender(String template) {
        if ("edemsa".equals(template)) {
            return EdemsaAnalyzeExecutor.class.getName();
        }
        return null;
    }
}
