package tech.nilanjan.cloud.google.spanner.SpannerDemo;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpannerDemoApplication {
	public static void main(String[] args) {
		try {
			CloudSpannerPlayground cloudSpannerPlayground = new CloudSpannerPlayground();

			/**
			 *
			 * Create Database
			 *
			 */
			cloudSpannerPlayground.createDatabaseAndTables();

			/**
			 *
			 * Insert New Data
			 *
			 */
			cloudSpannerPlayground.insertData();

			/**
			 *
			 * Read Data Only
			 *
			 */
			cloudSpannerPlayground.readOnlyFunc("IN");

			/**
			 *
			 * Read-Write Transaction
			 *
			 */
			cloudSpannerPlayground.readWriteTXN("nilanjan@gmail.com", "Nil Deb");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
