package br.unifor.np2;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.ec2.model.Instance;

public class Dados {

	private String id;
	private String status;
	private String itype;
	private String name;

	public static Dados fromInstance(Instance instance) {
		Dados data = new Dados();
		data.id = instance.getInstanceId();
		data.status = instance.getState().getName();
		data.itype = instance.getInstanceType();
		data.name = instance.getPlatform();
		return data;
	}

	public static List<Dados> fromInstances(List<Instance> instances) {
		List<Dados> list = new ArrayList<Dados>();
		for (Instance instance : instances) {
			Dados data = new Dados();
			data.id = instance.getInstanceId();
			data.status = instance.getState().getName();
			data.itype = instance.getInstanceType();
			data.name = instance.getPlatform();

			list.add(data);
		}
		return list;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getItype() {
		return itype;
	}

	public void setItype(String itype) {
		this.itype = itype;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ID: " + id + "  	  ; Status: " + status + "			; Instance Type: " + itype + "         ; Nome: "
				+ name;
	}
	// http://stackoverflow.com/questions/17313415/adding-data-dynamically-to-the-extended-class-of-abstracttablemodel
	// http://codinglogbook.com/tag/jcheckbox/
}