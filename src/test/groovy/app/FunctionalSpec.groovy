package app

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import ratpack.groovy.test.GroovyRatpackMainApplicationUnderTest
import spock.lang.AutoCleanup
import spock.lang.Specification

class FunctionalSpec extends Specification {

  @AutoCleanup
  @Delegate
  GroovyRatpackMainApplicationUnderTest aut = new GroovyRatpackMainApplicationUnderTest()

  def mapper = new ObjectMapper()

  void "should choose proper datasource according to request URI"() {
    when:
    def response = httpClient.get("/admin")

    and:
    def rows = (List<Map>)mapper.readValue(response.body.text, new TypeReference<List<Map>>() {})

    then:
    rows*.NAME == TestDbInit.namesDB1

    when:
    response = httpClient.get("/user")

    and:
    rows = (List<Map>)mapper.readValue(response.body.text, new TypeReference<List<Map>>() {})

    then:
    rows*.NAME == TestDbInit.namesDB2
  }
}
