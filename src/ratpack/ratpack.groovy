import app.DataSourceRegistry
import app.DataSourcesModule
import app.TestDbInit
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import ratpack.handling.Context

import javax.sql.DataSource
import ratpack.exec.*
import ratpack.handling.Handler

import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json

ratpack {
  serverConfig {
    yaml("/config.yml")
    sysProps()
    env()
  }
  bindings {
    moduleConfig(DataSourcesModule, serverConfig.get("", DataSourcesModule.Config))
    bindInstance new TestDbInit()
    bindInstance new NamesListHandler()
  }
  handlers {
    all { DataSourceRegistry dataSourceRegistry ->
      if (request.uri.startsWith("/admin")) {
        request.add(DataSource, dataSourceRegistry.get("ds1"))
      } else {
        request.add(DataSource, dataSourceRegistry.get("ds2"))
      }
      next()
    }

    get("admin", NamesListHandler)
    get("user", NamesListHandler)
  }
}

class NamesListHandler implements Handler {

  @Override
  void handle(Context ctx) throws Exception {
    def ds = ctx.request.get(DataSource)
    NamesService.getNames(ds).then { rows ->
      ctx.render(json(rows))
    }
  }
}

class NamesService {
  static Promise<List<GroovyRowResult>> getNames(DataSource ds) {
    def sql = new Sql(ds)
    Blocking.get {
      sql.rows("SELECT * FROM TEST")
    }
  }
}
