package tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.spanner;

import com.google.cloud.spring.data.spanner.core.admin.SpannerDatabaseAdminTemplate;
import com.google.cloud.spring.data.spanner.core.admin.SpannerSchemaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.io.entity.UserEntity;

@Component
public class SpannerSchemaTools {
    private final SpannerDatabaseAdminTemplate spannerDatabaseAdminTemplate;
    private final SpannerSchemaUtils spannerSchemaUtils;

    @Autowired
    public SpannerSchemaTools(
            SpannerDatabaseAdminTemplate spannerDatabaseAdminTemplate,
            SpannerSchemaUtils spannerSchemaUtils
    ) {
        this.spannerDatabaseAdminTemplate = spannerDatabaseAdminTemplate;
        this.spannerSchemaUtils = spannerSchemaUtils;
    }

    public void createTableIfNotExists() {
        if (!this.spannerDatabaseAdminTemplate.tableExists("users")) {
            this.spannerDatabaseAdminTemplate.executeDdlStrings(
                    this.spannerSchemaUtils
                            .getCreateTableDdlStringsForInterleavedHierarchy(UserEntity.class),
                    true);
        }
    }
}
