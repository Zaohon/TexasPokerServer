package cn.blockmc.Zao_hon;

import java.util.List;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

public class InfluxDB2Example {
  public static void main(final String[] args) {

    // You can generate a Token from the "Tokens Tab" in the UI
    String token = "IoaCWFt7CnaqIIQWGAoRp9loLj1vIDDYSQdsbjDJgZqpdcajf_gQmJ30PuKhZ2rw-I9GWhPo431rSrO1ShYzNQ==";
    String org = "pylon";

    try (InfluxDBClient client = InfluxDBClientFactory.create("http://localhost:8086", token.toCharArray())) {

      String query = "option v = {timeRangeStart: -3h, timeRangeStop: now()}\n" +
"\n" +
"from(bucket: \"ems\")\n" +
"    |> range(start: v.timeRangeStart, stop: v.timeRangeStop)\n" +
"    |> filter(fn: (r) => r[\"_measurement\"] == \"data\")\n" +
"    |> filter(\n" +
"        fn: (r) =>\n" +
"            r[\"_field\"] == \"pv_meter/ActivePower\" or r[\"_field\"] == \"dds5188/ActivePower\"\n" +
"                or\n" +
"                r[\"_field\"] == \"ess0/ActivePower\" or r[\"_field\"] == \"grid/ActivePower\",\n" +
"    )\n" +
"    |> filter(fn: (r) => r[\"edge\"] == \"0\")\n" +
"";
      List<FluxTable> tables = client.getQueryApi().query(query, org);

      for (FluxTable table : tables) {
        for (FluxRecord record : table.getRecords()) {
          System.out.println(record);
        }
      }
    }
  }
}

