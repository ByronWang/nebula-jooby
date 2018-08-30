package nebula.module.user;

public class Page {
	long id;
	String name;
	String menuLabel;
	String title;
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getMenuLabel() {
		return menuLabel;
	}
	public String getTitle() {
		return title;
	}
	public Page(long id, String name, String menuLabel, String title) {
		super();
		this.id = id;
		this.name = name;
		this.menuLabel = menuLabel;
		this.title = title;
	}
	
}
