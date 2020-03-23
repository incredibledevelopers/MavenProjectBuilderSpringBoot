package com.ipen.test;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.apache.commons.io.FileUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SuppressWarnings("serial")
@SpringBootApplication
public class DemoApplication extends JFrame implements ActionListener {

	public static void main(String[] args) throws MavenInvocationException {
		DemoApplication window = new DemoApplication();
		window.setTitle("Project Builder");
		window.pack();
		window.show();
		window.setMinimumSize(new Dimension(450, 0));
		window.setMaximumSize(new Dimension(450, Integer.MAX_VALUE));
		window.setResizable(false);
	}

	JLabel lblProjectName;
	JTextField txtProjectName;
	JLabel lblProjectPath;
	JButton go;
	JFileChooser chooser;
	JLabel lblDatabaseList;
	JComboBox cbDatabaseNames;
	JButton buildBtn;
	JLabel lblProjectType;
	JComboBox cbProjectType;
	JLabel lblPackageName;
	JTextField txtPackageName;
	JButton btnTest;
	JLabel lblMsg;
	JPanel panel_1;
	JButton button_1;
	String targetFileName;
	String choosertitle;
	String directoryDir;
	String selectedDir;
	JTextField txtFilePath;

	public DemoApplication() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("src/main/resources/image.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Image dimg = img.getScaledInstance(450, 450, Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(dimg);
		setContentPane(new JLabel(imageIcon));
		testLayout customLayout = new testLayout();
		Border blackline = BorderFactory.createLineBorder(Color.black);

		getContentPane().setFont(new Font("Helvetica", Font.PLAIN, 12));
		getContentPane().setLayout(customLayout);

		lblProjectName = new JLabel("Project Name");
		getContentPane().add(lblProjectName);

		txtProjectName = new JTextField("");
		getContentPane().add(txtProjectName);

		lblProjectPath = new JLabel("Select Your Project");
		getContentPane().add(lblProjectPath);

		Icon warnIcon = new ImageIcon("src/main/resources/open.gif");
		go = new JButton(warnIcon);
		getContentPane().add(go);
		go.addActionListener(this);

		lblDatabaseList = new JLabel("Database Name");
		getContentPane().add(lblDatabaseList);

		cbDatabaseNames = new JComboBox();
		cbDatabaseNames.addItem("Select");
		cbDatabaseNames.addItem("Oracle");
		cbDatabaseNames.addItem("Mysql");
		cbDatabaseNames.addItem("Postgresql");
		cbDatabaseNames.addItem("MongoDB");
		getContentPane().add(cbDatabaseNames);

		cbDatabaseNames.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String a = (String) cbDatabaseNames.getItemAt(cbDatabaseNames.getSelectedIndex());
				addDatabaseDependency(a);
			}
		});

		buildBtn = new JButton("Build");
		getContentPane().add(buildBtn);

		buildBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					buildProject();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		lblProjectType = new JLabel("Project Type");
		getContentPane().add(lblProjectType);

		cbProjectType = new JComboBox();
		cbProjectType.addItem("Web");
		cbProjectType.addItem("REST");
		getContentPane().add(cbProjectType);

		lblPackageName = new JLabel("Package Name");
		getContentPane().add(lblPackageName);

		txtPackageName = new JTextField("");
		getContentPane().add(txtPackageName);

		btnTest = new JButton("Test");
		getContentPane().add(btnTest);

		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testProject();
			}
		});

		lblMsg = new JLabel("Please Wait....");
		getContentPane().add(lblMsg);
		lblMsg.setVisible(false);

		panel_1 = new JPanel();
		getContentPane().add(panel_1);
		panel_1.setBorder(blackline);
		panel_1.setVisible(false);

		button_1 = new JButton("button_1");
		getContentPane().add(button_1);
		button_1.setVisible(false);

		txtFilePath = new JTextField("");
		getContentPane().add(txtFilePath);
		txtFilePath.setEnabled(false);

		setSize(getPreferredSize());

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public void buildProject() throws IOException {
		if (!txtPackageName.getText().equals("") && !txtProjectName.getText().equals("")) {
			panel_1.setVisible(false);
			button_1.setVisible(false);
			buildBtn.setEnabled(false);
			btnTest.setEnabled(false);
			lblMsg.setVisible(true);
			try {
				String cmd1 = "cd " + selectedDir + " && mvn clean install";
				String command = "cmd /c start cmd.exe /K \" " + cmd1 + "\" ";
				Runtime rt = Runtime.getRuntime();
				Process proc = rt.exec(command);
				BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				StringBuffer output = new StringBuffer();
				String line = "";
				while ((line = reader.readLine()) != null) {
					output.append(line + "\n");
				}
				this.getTargetFileNameAndPath();
			} catch (Exception e) {
				System.out.println("HEY Buddy ! U r Doing Something Wrong ");
				e.printStackTrace();
			}

		} else
			JOptionPane.showMessageDialog(this, "Please enter Project and Package name", "Alert",
					JOptionPane.WARNING_MESSAGE);
		buildBtn.setEnabled(true);
		btnTest.setEnabled(true);
	}

	public void testProject() {
		if (!txtPackageName.getText().equals("") && !txtProjectName.getText().equals("")) {
			buildBtn.setEnabled(false);
			btnTest.setEnabled(false);
			try {
				String cmd1 = "cd " + selectedDir + " && mvn compile";
				String command = "cmd /c start cmd.exe /K \" " + cmd1 + "\" ";
				Runtime rt = Runtime.getRuntime();
				Process proc = rt.exec(command);
				BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				StringBuffer output = new StringBuffer();
				String line = "";
				while ((line = reader.readLine()) != null) {
					output.append(line + "\n");
				}
			} catch (Exception e) {
				System.out.println("HEY Buddy ! U r Doing Something Wrong ");
				e.printStackTrace();
			}

		} else
			JOptionPane.showMessageDialog(this, "Please enter Project and Package name", "Alert",
					JOptionPane.WARNING_MESSAGE);

		buildBtn.setEnabled(true);
		btnTest.setEnabled(true);
	}

	public void addDatabaseDependency(String dbName) {
		try {
			MavenXpp3Reader mavenreader = new MavenXpp3Reader();
			Model model = mavenreader.read(new FileReader(new File(targetFileName)));
			Writer writer = new FileWriter(targetFileName);
			ArrayList<Dependency> dependencyList = new ArrayList<Dependency>();
			List<Dependency> existingDependencyList = model.getDependencies();
			dependencyList.addAll(existingDependencyList);

			if (dbName.equals("MongoDB")) {
				Dependency dep = new Dependency();
				dep.setGroupId("org.springframework.boot");
				dep.setArtifactId("spring-boot-starter-data-mongodb");
				dependencyList.add(dep);
			}

			else if (dbName.equals("Oracle")) {
				Dependency dep = new Dependency();
				dep.setGroupId("com.oracle");
				dep.setArtifactId("ojdbc14");
				dep.setVersion("10.2.0.4.0");
				dependencyList.add(dep);
			}

			else if (dbName.equals("Postgresql")) {
				Dependency dep = new Dependency();
				dep.setGroupId("org.postgresql");
				dep.setArtifactId("postgresql");
				dep.setVersion("42.2.10");
				dependencyList.add(dep);
			}

			else if (dbName.equals("Mysql")) {
				Dependency dep = new Dependency();
				dep.setGroupId("mysql");
				dep.setArtifactId("mysql-connector-java");
				// dep.setVersion("5.1.48");
				dependencyList.add(dep);
			}

			model.setDependencies(dependencyList);
			new MavenXpp3Writer().write(writer, model);
			writer.close();

		} catch (IOException | XmlPullParserException e1) {
			e1.printStackTrace();
		}
	}

	public void getTargetFileNameAndPath() throws IOException {
		File dir = new File(directoryDir);
		String[] extensions = new String[] { "original" };
		List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
		String targetFile = null;
		for (File file : files) {
			targetFile = file.getName().replaceFirst(".original", "");
		}
		if (targetFile != null && !targetFile.equals("")) {
			button_1.setText(targetFile);
			lblMsg.setVisible(false);
			panel_1.setVisible(true);
			button_1.setVisible(true);
		} else {
			panel_1.setVisible(false);
			button_1.setVisible(false);
		}
	}

	public void actionPerformed(ActionEvent e) {

		if (!txtPackageName.getText().equals("") && !txtProjectName.getText().equals("")) {
			chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle(choosertitle);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);

			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				String source = "src/main/resources/pom.xml";
				selectedDir = chooser.getSelectedFile().toString();
				txtFilePath.setText(selectedDir);
				String target = selectedDir.replaceAll("\\\\", "\\\\\\\\");
				directoryDir = target + "\\target\\";
				File sourceFile = new File(source);
				String name = sourceFile.getName();
				targetFileName = target + "\\" + name;
				File targetFile = new File(targetFileName);
				try {

					File srcDir = new File(selectedDir);
					String destination = selectedDir + "\\src\\main\\java\\";
					String packageName = txtPackageName.getText();
					String[] arrSplit = packageName.split("\\.");
					for (int i = 0; i < arrSplit.length; i++) {
						destination = destination + "\\" + arrSplit[i];
					}
					System.out.println("Destination :" + destination);
					File destDir = new File(destination);

					move(srcDir, destDir);

					File oneMoreDirectory = new File(selectedDir + "\\src\\main" + File.separator + "resources");
					boolean isCreated = oneMoreDirectory.mkdir();
					if (isCreated) {
						System.out.printf("\n3. Successfully created new directory, path:%s",
								oneMoreDirectory.getCanonicalPath());
					} else { // Directory may already exist
						System.out.printf("\n3. Unable to create directory");
					}

					File propDir = new File(destination + "\\application.properties");
					File yamlDir = new File(destination + "\\application.yaml");

					FileSystem fileSys = FileSystems.getDefault();
					
					try {
						if(propDir.exists()) {
							Path srcPath = fileSys.getPath(destination+"\\application.properties");
							Path destPath = fileSys.getPath(selectedDir+"\\src\\main\\resources\\application.properties");
							Files.move(srcPath, destPath, StandardCopyOption.REPLACE_EXISTING);
						}
						else if(yamlDir.exists()) {
							Path srcPath = fileSys.getPath(destination+"\\application.yaml");
							Path destPath = fileSys.getPath(selectedDir+"\\src\\main\\resources\\application.yaml");
							Files.move(srcPath, destPath, StandardCopyOption.REPLACE_EXISTING);
						}
						
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

					FileUtils.copyFile(sourceFile, targetFile);
					MavenXpp3Reader mavenreader = new MavenXpp3Reader();
					Model model = mavenreader.read(new FileReader(new File(targetFileName)));
					model.setGroupId(txtPackageName.getText());
					model.setArtifactId(txtProjectName.getText());
					model.setName(txtProjectName.getText());
					Writer writer = new FileWriter(targetFileName);
					new MavenXpp3Writer().write(writer, model);
					writer.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			} else {
				System.out.println("No Selection ");
			}
		} else
			JOptionPane.showMessageDialog(this, "Please enter Project and Package name", "Alert",
					JOptionPane.WARNING_MESSAGE);
	}

	private void move(File sourceFile, File destFile) {
		if (sourceFile.isDirectory()) {
			File[] files = sourceFile.listFiles();

			assert files != null;
			for (File file : files) {
				// if(file.getName().endsWith(".yaml") ||
				// file.getName().endsWith(".properties"))
				move(file, new File(destFile, file.getName()));
			}

			sourceFile.delete();

		} else {
			if (!destFile.getParentFile().exists())
				if (!destFile.getParentFile().mkdirs())
					throw new RuntimeException();
			if (!sourceFile.renameTo(destFile))
				throw new RuntimeException();
		}
	}

}

class testLayout implements LayoutManager {

	public testLayout() {
	}

	public void addLayoutComponent(String name, Component comp) {
	}

	public void removeLayoutComponent(Component comp) {
	}

	public Dimension preferredLayoutSize(Container parent) {
		Dimension dim = new Dimension(0, 0);

		Insets insets = parent.getInsets();
		dim.width = 450 + insets.left + insets.right;
		dim.height = 450 + insets.top + insets.bottom;

		return dim;
	}

	public Dimension minimumLayoutSize(Container parent) {
		Dimension dim = new Dimension(0, 0);
		return dim;
	}

	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();

		Component c;
		c = parent.getComponent(0);
		if (c.isVisible()) {
			c.setBounds(insets.left + 40, insets.top + 32, 168, 24);
		}
		c = parent.getComponent(1);
		if (c.isVisible()) {
			c.setBounds(insets.left + 224, insets.top + 32, 144, 24);
		}
		c = parent.getComponent(2);
		if (c.isVisible()) {
			c.setBounds(insets.left + 40, insets.top + 128, 168, 24);
		}
		c = parent.getComponent(3);
		if (c.isVisible()) {
			c.setBounds(insets.left + 224, insets.top + 128, 32, 24);
		}
		c = parent.getComponent(4);
		if (c.isVisible()) {
			c.setBounds(insets.left + 40, insets.top + 160, 168, 24);
		}
		c = parent.getComponent(5);
		if (c.isVisible()) {
			c.setBounds(insets.left + 224, insets.top + 160, 144, 24);
		}
		c = parent.getComponent(6);
		if (c.isVisible()) {
			c.setBounds(insets.left + 224, insets.top + 200, 72, 24);
		}
		c = parent.getComponent(7);
		if (c.isVisible()) {
			c.setBounds(insets.left + 40, insets.top + 64, 168, 24);
		}
		c = parent.getComponent(8);
		if (c.isVisible()) {
			c.setBounds(insets.left + 224, insets.top + 64, 144, 24);
		}
		c = parent.getComponent(9);
		if (c.isVisible()) {
			c.setBounds(insets.left + 40, insets.top + 96, 168, 24);
		}
		c = parent.getComponent(10);
		if (c.isVisible()) {
			c.setBounds(insets.left + 224, insets.top + 96, 144, 24);
		}
		c = parent.getComponent(11);
		if (c.isVisible()) {
			c.setBounds(insets.left + 112, insets.top + 200, 72, 24);
		}
		c = parent.getComponent(12);
		if (c.isVisible()) {
			c.setBounds(insets.left + 96, insets.top + 232, 232, 24);
		}
		c = parent.getComponent(13);
		if (c.isVisible()) {
			c.setBounds(insets.left + 40, insets.top + 264, 320, 136);
		}
		c = parent.getComponent(14);
		if (c.isVisible()) {
			c.setBounds(insets.left + 48, insets.top + 272, 152, 32);
		}
		c = parent.getComponent(15);
		if (c.isVisible()) {
			c.setBounds(insets.left + 264, insets.top + 128, 104, 24);
		}
	}
}