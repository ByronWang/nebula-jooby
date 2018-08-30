package nebula.module.user;

public class SimpleUser {

	private Long id;

	private String name;
	
	private String desciption;


	public SimpleUser(Long id, String name, String desciption) {
		super();
		this.id = id;
		this.name = name;
		this.desciption = desciption;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDesciption() {
		return desciption;
	}
}
