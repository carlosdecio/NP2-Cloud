package br.unifor.np2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.amazonaws.services.s3.model.S3ObjectSummary;

public class ArquivoDado {

	private String name;
	

	public ArquivoDado(String name) {
		this.name = name;
	}

	public static ArquivoDado fromSummary(S3ObjectSummary summary) {
		ArquivoDado data = new ArquivoDado(summary.getKey());
		return data;
	}

	public static List<ArquivoDado> fromSummaries(List<S3ObjectSummary> summaries) {
		List<ArquivoDado> data = new ArrayList<ArquivoDado>();
		for (S3ObjectSummary s : summaries) {
			data.add(new ArquivoDado(s.getKey()));
		}
		return data;
	}
	Date data = new Date();
	String dStr = java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(data);
	Calendar c = Calendar.getInstance();
	@Override
	public String toString() {
		return "Arquivo Upload: " + name + " - " + dStr;
	}

	public String getName() {
		return name;
	}

}
