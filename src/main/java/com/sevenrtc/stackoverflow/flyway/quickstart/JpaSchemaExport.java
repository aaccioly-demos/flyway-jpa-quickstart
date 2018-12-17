package com.sevenrtc.stackoverflow.flyway.quickstart;

import org.hibernate.cfg.AvailableSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Persistence;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

// Adapted from https://stackoverflow.com/a/27313680/664577
public class JpaSchemaExport {

    final static Logger logger = LoggerFactory.getLogger(JpaSchemaExport.class);

    public static void main(String[] args) {
        execute(args[0], args[1]);
        System.exit(0);
    }

    public static void execute(String persistenceUnitName, String destination) {
        logger.info("Using PU: {}", persistenceUnitName);
        logger.info("Generating DDL create script to : {}", destination);

        deleteIfExists(destination);

        final Properties persistenceProperties = new Properties();
        // javax.persistence.schema-generation.scripts.action
        persistenceProperties.setProperty(AvailableSettings.HBM2DDL_SCRIPTS_ACTION, "update");
        // javax.persistence.schema-generation.scripts.create-target
        persistenceProperties.setProperty(AvailableSettings.HBM2DDL_SCRIPTS_CREATE_TARGET, destination);
        // hibernate.hbm2ddl.delimiter
        persistenceProperties.setProperty(AvailableSettings.HBM2DDL_DELIMITER, ";");

        Persistence.generateSchema(persistenceUnitName, persistenceProperties);
    }

    private static void deleteIfExists(String destination) {
        Path filePath = Paths.get(destination);
        try {
            if (Files.deleteIfExists(filePath)) {
                logger.debug("Deleted existing file at {}", filePath);
            }
        } catch (IOException e) {
            logger.error("I/O error occurred trying to delete \"{}\"", destination, e);
        }
    }

}
