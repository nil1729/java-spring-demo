package tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.spanner;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.spanner.*;
import com.google.spanner.admin.database.v1.CreateDatabaseMetadata;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class SpannerEmulatorPlayground {
    public static void main(String[] args) {
        String projectId = "pwa-demo-nil1729";
        String instanceId = "spring-security-spanner";
        String databaseId = "users";

        SpannerOptions spannerOptions = SpannerOptions
                .newBuilder()
                .setEmulatorHost("localhost:9010")
                .build();
        Spanner spanner = spannerOptions.getService();
        DatabaseAdminClient databaseAdminClient = spanner.getDatabaseAdminClient();

        /**
         *
         * Creating Database
         *
         */
        final Database databaseToCreate = databaseAdminClient
                .newDatabaseBuilder(DatabaseId.of(projectId, instanceId, databaseId))
                .build();
        final OperationFuture<Database, CreateDatabaseMetadata> operationDatabase =
                databaseAdminClient
                        .createDatabase(
                                databaseToCreate,
                                Arrays.asList(
                                        "CREATE TABLE Users (" +
                                                "   UserId INT64 NOT NULL," +
                                                "   Username STRING(100) NOT NULL," +
                                                "   Password STRING(100) NOT NULL," +
                                                "   FirstName STRING(1024)," +
                                                "   LastName STRING(1024)," +
                                                "   MobileNumber STRING(15)," +
                                                ") PRIMARY KEY (UserId)",
                                        "CREATE UNIQUE INDEX UniqueUsername ON Users(Username)",
                                        "CREATE INDEX Login ON Users(Username, Password)"
                                )
                        );

        try {
            // Initiate the request which returns on OperationFuture
            Database database = operationDatabase.get();
            System.out.println("Created Database [" + database.getId() + "]");
        } catch (ExecutionException ex) {
            ex.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        /**
         *
         * Database Client
         *
         */
        DatabaseClient databaseClient = spanner.getDatabaseClient(DatabaseId.of(projectId, instanceId, databaseId));

        /**
         *
         * Insert Data
         *
         */
        SpannerEmulatorPlayground.createUser(
                databaseClient,
                "nil1729",
                "password",
                "Nilanjan",
                "Deb",
                "9999988888"
        );

        /**
         *
         * Login User
         *
         */
        SpannerEmulatorPlayground.loginUser(databaseClient, "nil1729", "password");
    }

    public static void createUser(
            DatabaseClient databaseClient,
            String Username,
            String Password,
            String FirstName,
            String LastName,
            String MobileNumber
    ) {
        databaseClient
                .readWriteTransaction()
                .run(transactionContext -> {
                    int UserId = new Random().nextInt(1, 1000);

                    String sql = String.format("INSERT INTO Users (" +
                                    "UserId, " +
                                    "Username, " +
                                    "Password, " +
                                    "FirstName, " +
                                    "LastName, " +
                                    "MobileNumber" +
                                    ") VALUES (%s, '%s', '%s', '%s', '%s', '%s')",
                            UserId, Username, Password, FirstName, LastName, MobileNumber);

                    long rowCount = transactionContext.executeUpdate(Statement.of(sql));
                    System.out.printf("%d record inserted\n", rowCount);

                    return null;
                });
    }

    public static void loginUser(
            DatabaseClient databaseClient,
            String Username,
            String Password
    ) {
        databaseClient.readWriteTransaction()
                .run(transactionContext -> {
                    // Read inserted record
                    String sql = String.format("" +
                            "SELECT " +
                            "   FirstName, " +
                            "   LastName, " +
                            "   MobileNumber " +
                            "FROM Users " +
                            "   WHERE Username='%s' AND PASSWORD='%s'",
                            Username, Password
                    );

                    // We use a try-with-resource block to automatically release resources held by ResultSet
                    try(ResultSet resultSet = transactionContext.executeQuery(Statement.of(sql))) {
                        while (resultSet.next()) {
                            System.out.printf("User found as: %s %s - %s\n",
                                    resultSet.getString("FirstName"),
                                    resultSet.getString("LastName"),
                                    resultSet.getString("MobileNumber")
                            );
                        }
                    }
                    return null;
                });
    }
}
