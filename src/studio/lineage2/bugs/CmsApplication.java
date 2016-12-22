package studio.lineage2.bugs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import studio.lineage2.bugs.xml.Project;
import studio.lineage2.bugs.xml.Result;
import studio.lineage2.bugs.xml.Type;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@SpringBootApplication @EnableAutoConfiguration @ComponentScan @EnableScheduling @EnableWebMvc public class CmsApplication
{
	private static Map<Integer, Project> projects = new TreeMap<>();
	private static Map<Integer, Type> types = new TreeMap<>();
	private static List<Result> skillNames = new ArrayList<>();
	private static List<Result> questNames = new ArrayList<>();
	private static List<Result> instanceNames = new ArrayList<>();
	private static List<Result> monsterNames = new ArrayList<>();
	private static List<Result> itemNames = new ArrayList<>();

	public static Map<Integer, Project> getProjects()
	{
		return projects;
	}
	public static Map<Integer, Type> getTypes()
	{
		return types;
	}
	public static List<Result> getSkillNames()
	{
		return skillNames;
	}
	public static List<Result> getQuestNames()
	{
		return questNames;
	}
	public static List<Result> getInstanceNames()
	{
		return instanceNames;
	}
	public static List<Result> getMonsterNames()
	{
		return monsterNames;
	}
	public static List<Result> getItemNames()
	{
		return itemNames;
	}

	public static void main(String[] args)
	{
		loadProjects();
		loadTypes();

		load("skill_names.txt", skillNames);
		load("quest_names.txt", questNames);
		load("instance_names.txt", instanceNames);
		load("monster_names.txt", monsterNames);
		load("item_names.txt", itemNames);

		SpringApplication.run(CmsApplication.class, args);
	}

	private static void loadProjects()
	{
		try
		{
			File fXmlFile = new File("./public/data/projects.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList root = doc.getElementsByTagName("project");

			for(int i = 0; i < root.getLength(); i++)
			{
				Node node = root.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE)
				{
					Element element = (Element) node;
					Project project = new Project();
					project.setId(Integer.parseInt(element.getAttribute("id")));
					project.setName(element.getAttribute("name"));
					projects.put(project.getId(), project);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void loadTypes()
	{
		try
		{
			File fXmlFile = new File("./public/data/types.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList root = doc.getElementsByTagName("type");

			for(int i = 0; i < root.getLength(); i++)
			{
				Node node = root.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE)
				{
					Element element = (Element) node;
					Type type = new Type();
					type.setId(Integer.parseInt(element.getAttribute("id")));
					type.setName(element.getAttribute("name"));
					types.put(type.getId(), type);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void load(String fileName, List<Result> results)
	{
		Set<String> strings = new HashSet<>();

		File file = new File("./public/data/" + fileName);
		if(file.exists())
		{
			LineNumberReader reader = null;
			try
			{
				reader = new LineNumberReader(new FileReader(file));
				String line;
				while((line = reader.readLine()) != null)
				{
					String text = line.replace("a,", "").replace("u,", "").replace("\\0", "");
					strings.add(text);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					reader.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		for(String string : strings)
		{
			Result result = new Result();
			result.setValue(string);
			result.setName(string);
			results.add(result);
		}
	}
}