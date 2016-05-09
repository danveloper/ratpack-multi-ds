package app

import com.google.inject.Provides
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import ratpack.guice.ConfigurableModule
import ratpack.hikari.HikariService

class DataSourcesModule extends ConfigurableModule<Config> {

  @Override
  protected void configure() {

  }

  @Provides
  DataSourceRegistry registry(Config config) {
    def registry = new DataSourceRegistry()
    config.datasources.each { name, c ->
      def service = new HikariService(new HikariDataSource(new HikariConfig(jdbcUrl: c.url, username: c.user, password: c.pass)))
      registry.register(name, service.dataSource)
    }
    registry
  }

  static class Config {
    Map<String, DataSourceConfig> datasources
  }

  static class DataSourceConfig {
    String name
    String url
    String user
    String pass
  }
}
