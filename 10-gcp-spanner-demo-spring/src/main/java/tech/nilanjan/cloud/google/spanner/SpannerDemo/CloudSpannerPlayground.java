package tech.nilanjan.cloud.google.spanner.SpannerDemo;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.spanner.*;
import com.google.spanner.admin.database.v1.CreateDatabaseMetadata;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CloudSpannerPlayground {
    private final Spanner spanner;
    private final String instanceId = "demo-spring-spanner";
    private final String databaseId = "demo-db";

    public CloudSpannerPlayground() throws IOException {
        GoogleCredentials.getApplicationDefault();
        SpannerOptions spannerOptions = SpannerOptions.newBuilder().build();
        spanner = spannerOptions.getService();
    }

    public void createDatabaseAndTables() throws InterruptedException {
        DatabaseAdminClient databaseAdminClient = this.spanner.getDatabaseAdminClient();

        OperationFuture<Database, CreateDatabaseMetadata> database = databaseAdminClient.createDatabase(
                this.instanceId,
                this.databaseId,
                Arrays.asList(
                        "CREATE TABLE Account ( " +
                                "AccountId      STRING(36) NOT NULL, " +
                                "Name           STRING(256) NOT NULL, " +
                                "Email          STRING(256) NOT NULL " +
                                ") PRIMARY KEY (AccountId)",
                        "CREATE TABLE Address ( " +
                                "AccountId      STRING(36) NOT NULL, " +
                                "AddressId      STRING(36) NOT NULL, " +
                                "Street         STRING(1024) NOT NULL, " +
                                "City           STRING(256) NOT NULL, " +
                                "ZIP            STRING(6) NOT NULL, " +
                                "CountryCode    STRING(2) NOT NULL " +
                                ") PRIMARY KEY (AccountId, AddressId), " +
                                "INTERLEAVE IN PARENT Account ON DELETE CASCADE",
                        "CREATE INDEX ByCountry ON Address(CountryCode) STORING (City, ZIP)",
                        "CREATE UNIQUE NULL_FILTERED INDEX EmailIndex ON Account(Email)"
                )
        );

        synchronized (database) {
            try {
                database.wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void insertData() {
        DatabaseClient databaseClient = spanner.getDatabaseClient(
                DatabaseId.of(
                        spanner.getOptions().getProjectId(),
                        this.instanceId,
                        this.databaseId
                )
        );

        List<Mutation> mutations = new ArrayList<>();

        /**
         *
         * Accounts
         *
         */
        String accountIdOne = UUID.randomUUID().toString();
        String accountIdTwo = UUID.randomUUID().toString();
        String accountIdThree = UUID.randomUUID().toString();

        /**
         *
         * Account 1
         *
         */
        mutations.add(
                Mutation
                        .newInsertBuilder("Account")
                        .set("AccountId").to(accountIdOne)
                        .set("Name").to("Nilanjan")
                        .set("Email").to("nilanjan@gmail.com")
                        .build()
        );

        /**
         *
         * Account 2
         *
         */
        mutations.add(
                Mutation
                        .newInsertBuilder("Account")
                        .set("AccountId").to(accountIdTwo)
                        .set("Name").to("John")
                        .set("Email").to("john@gmail.com")
                        .build()
        );

        /**
         *
         * Account 3
         *
         */
        mutations.add(
                Mutation
                        .newInsertBuilder("Account")
                        .set("AccountId").to(accountIdThree)
                        .set("Name").to("Tim")
                        .set("Email").to("tim@gmail.com")
                        .build()
        );

        /**
         *
         * Address 1
         *
         */
        mutations.add(
                Mutation
                        .newInsertBuilder("Address")
                        .set("AccountId").to(accountIdOne)
                        .set("AddressId").to(UUID.randomUUID().toString())
                        .set("Street").to("CloudOnAir Cloud Spanner Avenue 101")
                        .set("City").to("Delhi")
                        .set("ZIP").to("RSTTSR")
                        .set("CountryCode").to("IN")
                        .build()
        );

        /**
         *
         * Address 2
         *
         */
        mutations.add(
                Mutation
                        .newInsertBuilder("Address")
                        .set("AccountId").to(accountIdOne)
                        .set("AddressId").to(UUID.randomUUID().toString())
                        .set("Street").to("CloudOnAir Cloud Spanner Avenue 201")
                        .set("City").to("Tokyo")
                        .set("ZIP").to("ABCCBA")
                        .set("CountryCode").to("JP")
                        .build()
        );

        /**
         *
         * Address 3
         *
         */
        mutations.add(
                Mutation
                        .newInsertBuilder("Address")
                        .set("AccountId").to(accountIdTwo)
                        .set("AddressId").to(UUID.randomUUID().toString())
                        .set("Street").to("CloudOnAir Cloud Spanner Avenue 301")
                        .set("City").to("Berlin")
                        .set("ZIP").to("XYZZYX")
                        .set("CountryCode").to("DE")
                        .build()
        );

        /**
         *
         * Address 4
         *
         */
        mutations.add(
                Mutation
                        .newInsertBuilder("Address")
                        .set("AccountId").to(accountIdThree)
                        .set("AddressId").to(UUID.randomUUID().toString())
                        .set("Street").to("CloudOnAir Cloud Spanner Avenue 401")
                        .set("City").to("Mumbai")
                        .set("ZIP").to("PQRRQP")
                        .set("CountryCode").to("IN")
                        .build()
        );

        databaseClient.write(mutations);
    }

    public void readOnlyFunc(String countryCode) {
        DatabaseClient databaseClient = spanner.getDatabaseClient(
                DatabaseId.of(
                        spanner.getOptions().getProjectId(),
                        instanceId,
                        databaseId
                )
        );

        ReadOnlyTransaction txn = databaseClient.readOnlyTransaction();

        ResultSet resultSet = txn.executeQuery(
                Statement
                        .newBuilder(
                                "SELECT " +
                                        "a.Name, a.Email " +
                                    "FROM " +
                                        "Account a, " +
                                        "Address ad " +
                                    "WHERE " +
                                        "a.AccountId = ad.AccountId AND " +
                                        "ad.CountryCode = @countryCode " +
                                    "LIMIT 5"
                        )
                        .bind("countryCode").to(countryCode)
                        .build()
        );

        while (resultSet.next()) {
            System.out.printf(
                    "%s - %s%n",
                    resultSet.getString(0),
                    resultSet.getString(1)
            );
        }
    }

    public void readWriteTXN(String email, String newName) {
        DatabaseClient databaseClient = spanner.getDatabaseClient(
                DatabaseId.of(
                        spanner.getOptions().getProjectId(),
                        instanceId,
                        databaseId
                )
        );

        databaseClient.readWriteTransaction().run(
                new TransactionRunner.TransactionCallable<Void>() {
                    @Nullable
                    @Override
                    public Void run(TransactionContext transactionContext) throws Exception {
                        ResultSet resultSet = transactionContext.executeQuery(
                                Statement
                                        .newBuilder(
                                                "SELECT * FROM Account WHERE Email = @emailAddress"
                                        )
                                        .bind("emailAddress").to(email)
                                        .build()
                        );

                        List<Mutation> mutations = new ArrayList<>();

                        while (resultSet.next()) {
                            mutations.add(
                                    Mutation
                                            .newUpdateBuilder("Account")
                                            .set("AccountId").to(resultSet.getString("AccountId"))
                                            .set("Name").to(newName)
                                            .set("Email").to(resultSet.getString("Email"))
                                            .build()
                            );
                        }

                        transactionContext.buffer(mutations);
                        return null;
                    }
                }
        );
    }
}
