package app

import groovy.sql.Sql
import ratpack.service.Service
import ratpack.service.StartEvent

import javax.sql.DataSource

class TestDbInit implements Service {

  static List<String> namesDB1 = [
      'Luke Daley', 'Rus Hart'
  ]

  static List<String> namesDB2 = [
      'Dan Woods', 'Danny Hyun'
  ]

  @Override
  void onStart(StartEvent e) {
    def dataSourceRegistry = e.registry.get(DataSourceRegistry)
    def ds1 = dataSourceRegistry.get("ds1")
    def ds2 = dataSourceRegistry.get("ds2")
    createTable ds1
    createTable ds2
    bootStrapDB ds1, namesDB1
    bootStrapDB ds2, namesDB2
  }

  void createTable(DataSource ds) {
    def sql = new Sql(ds)
    sql.execute("CREATE TABLE TEST(ID INT PRIMARY KEY AUTO_INCREMENT, NAME VARCHAR(255))")
  }

  void bootStrapDB(DataSource ds, List<String> names) {
    def sql = new Sql(ds)
    names.each { name ->
      sql.execute("insert into test (name) values ($name)")
    }
  }
}
