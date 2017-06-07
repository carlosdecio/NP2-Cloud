package br.unifor.np2;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;

public class Gerenciamento {

	private static Gerenciamento manager;
	private static AmazonEC2 ec2;

	public static String AMAZON_LINUX = "ami-c58c1dd3";
	public static String UBUNTU_SERVER = "ami-f4cc1de2";
	public static String WINDOWS_SERVER = "ami-ef7cf4f9";

	protected Gerenciamento() {
		AWSCredentials credentials = new ProfileCredentialsProvider("cloudnp2").getCredentials();
		ec2 = new AmazonEC2Client(credentials);
		ec2.setEndpoint("ec2.us-east-1.amazonaws.com");
	}

	public static Gerenciamento manager() {
		if (manager == null) {
			manager = new Gerenciamento();
		}
		return manager;
	}

	public List<Instance> getInstances() {
		List<Instance> instances = new ArrayList<Instance>();
		boolean done = false;
		while (!done) {
			DescribeInstancesRequest request = new DescribeInstancesRequest();
			DescribeInstancesResult response = ec2.describeInstances(request);
			for (Reservation reservation : response.getReservations()) {
				for (Instance instance : reservation.getInstances()) {
					instances.add(instance);
				}
			}
			request.setNextToken(response.getNextToken());
			if (response.getNextToken() == null) {
				done = true;
			}
		}
		return instances;
	}

	public String createInstance(String imageId) {
		RunInstancesRequest runInstancesRequest = new RunInstancesRequest();

		runInstancesRequest.withImageId(imageId).withInstanceType("t2.micro").withMinCount(1).withMaxCount(1)
				.withKeyName("windows server").withSecurityGroups("launch-wizard-1");
		String instanceId = ec2.runInstances(runInstancesRequest).getReservation().getReservationId();
		return instanceId;
	}

	public void startInstance(String id) {
		StartInstancesRequest request = new StartInstancesRequest().withInstanceIds(id);
		ec2.startInstances(request);
	}

	public void stopInstance(String id) {
		StopInstancesRequest request = new StopInstancesRequest().withInstanceIds(id);
		ec2.stopInstances(request);
	}

	public void terminateInstance(String id) {
		TerminateInstancesRequest request = new TerminateInstancesRequest().withInstanceIds(id);
		ec2.terminateInstances(request);
	}

}
