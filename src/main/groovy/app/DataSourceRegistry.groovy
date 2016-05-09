package app

import com.google.common.collect.Maps

import javax.sql.DataSource

class DataSourceRegistry {

  private final Map<String, DataSource> registry = Maps.newHashMap()

  void register(String name, DataSource dataSource) {
    registry[name] = dataSource
  }

  Optional<DataSource> maybeGet(String name) {
    registry.containsKey(name) ? Optional.of(registry[name]) : Optional.empty()
  }

  DataSource get(String name) {
    def o = maybeGet(name)
    if (o.present) {
      o.get()
    } else {
      throw new IllegalStateException("No datasource registered under $name")
    }
  }
}
