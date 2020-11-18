package test.prometheus.custom.exporter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MainController {

	private final Counter requestCount;
	private final Gauge gauge;
	private final Histogram histogram;
	private final Summary summary;
	private final Gauge gauge2;

	public MainController(CollectorRegistry collectorRegistry) {
		requestCount = Counter.build()
				.name("java_request_count")
				.help("Number of hello requests.")
				.register(collectorRegistry);
		gauge = Gauge.build().namespace("java").name("my_gauge").help("This is my gauge")
				.register(collectorRegistry);
		histogram = Histogram.build().namespace("java").name("my_histogram").help("This is my histogram")
				.register(collectorRegistry);
		summary = Summary.build()
				.namespace("java")
				.name("my_summary")
				.help("This is my summary")
				.register(collectorRegistry);
		
//		gauge2 = Gauge.build().namespace("OwO").labelNames("label01").name("QAQ").help("Wryyyy")
//				.register(collectorRegistry);
		
		gauge2 = Gauge.build().namespace("OwO").labelNames("label01","label02").name("QAQ").help("Wryyyy")
				.register(collectorRegistry);

	}

	protected static double rand(double min, double max) {
		return min + (Math.random() * (max - min));
	}

	@ResponseBody
	@GetMapping(value = "/test", produces = "application/json")
	public String test(HttpServletRequest request, HttpServletResponse response) {
		requestCount.inc();
//		this.counter.inc(rand(0, 5));
		gauge.set(rand(-5, 10));
//		gauge2.labels("QQ").set(rand(-5, 10));
		gauge2.labels("AA","BB").set(rand(-5, 10));
//		gauge.set(5);
		this.histogram.observe(rand(0, 5));
		this.summary.observe(rand(0, 5));
		return "Hi!";
	}
}
