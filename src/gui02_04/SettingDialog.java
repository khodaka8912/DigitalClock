package gui02_04;

import gui01_04.Consts;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class SettingDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final List<Integer> fontSizes = Settings.getFontSizeList();
	private final List<String> fonts = Settings.getFontList();
	private final Map<String, Color> colorMap = Settings.getColorMap();

	private JComboBox<String> fontSelector;
	private JCheckBox boldChecker;
	private JCheckBox italicChecker;
	private JComboBox<Integer> fontSizeSelector;
	private JComboBox<String> fontColorSelector;
	private JComboBox<String> bgColorSelector;
	private JButton okButton;
	private JButton cancelButton;

	private SettingsListener listener;

	public SettingDialog(Frame owner) {
		super(owner, Consts.Strings.SETTINGS, true);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});

		fontSelector = new JComboBox<>();
		fontSizeSelector = new JComboBox<>();
		fontColorSelector = new JComboBox<>();
		bgColorSelector = new JComboBox<>();

		fontSizes.stream().forEach(fontSizeSelector::addItem);
		fonts.stream().forEach(fontSelector::addItem);
		colorMap.keySet().stream().forEach(s -> {
			fontColorSelector.addItem(s);
			bgColorSelector.addItem(s);
		});
		ListCellRenderer<String> colorPicker = (list, value, idx, select, focus) -> {
			JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

			JLabel label = new JLabel(value);
			Color color = colorMap.get(value);
			JPanel chip = new JPanel();
			chip.setSize(10, 10);
			chip.setBackground(color);
			panel.add(chip);
			panel.add(label);
			if (select || focus) {
				panel.setForeground(Color.WHITE);
				panel.setBackground(Color.BLUE);
			} else {
				setForeground(Color.BLACK);
				setBackground(Color.WHITE);
			}

			return panel;
		};
		fontColorSelector.setRenderer(colorPicker);
		bgColorSelector.setRenderer(colorPicker);
		boldChecker = new JCheckBox(Consts.Strings.BOLD);
		italicChecker = new JCheckBox(Consts.Strings.ITALIC);

		okButton = new JButton("OK");
		okButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);

		// Layout
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		setSize(350, 250);
		setResizable(false);

//		add(new Label(Consts.Strings.FONT));
//		add(fontSelector);
//		add(boldChecker);
//		add(italicChecker);
//		add(new Label(Consts.Strings.FONT_SIZE));
//		add(fontSizeSelector);
//		add(new Label(Consts.Strings.FONT_COLOR));
//		add(fontColorSelector);
//		add(new Label(Consts.Strings.BG_COLOR));
//		add(bgColorSelector);
//		add(new Label(""));
//		add(okButton);

		GridBagConstraints constraints = new GridBagConstraints();

		Label fontLabel = new Label(Consts.Strings.FONT);
		Label sizeLabel = new Label(Consts.Strings.FONT_SIZE);
		Label fontColorLabel = new Label(Consts.Strings.FONT_COLOR);
		Label bgColorLabel = new Label(Consts.Strings.BG_COLOR);

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.EAST;
		layout.setConstraints(fontLabel, constraints);
		add(fontLabel);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.WEST;
		layout.setConstraints(fontSelector, constraints);		
		add(fontSelector);
		Panel checkPanel = new Panel();
		checkPanel.setLayout(new GridLayout(1, 2));
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.WEST;
		layout.setConstraints(checkPanel, constraints);
		add(checkPanel);
		checkPanel.add(boldChecker);	
		checkPanel.add(italicChecker);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.EAST;
		layout.setConstraints(sizeLabel, constraints);		
		add(sizeLabel);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.WEST;
		layout.setConstraints(fontSizeSelector, constraints);
		add(fontSizeSelector);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.EAST;
		layout.setConstraints(fontColorLabel, constraints);
		add(fontColorLabel);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.anchor = GridBagConstraints.WEST;
		layout.setConstraints(fontColorSelector, constraints);		
		add(fontColorSelector);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.EAST;
		layout.setConstraints(bgColorLabel, constraints);		
		add(bgColorLabel);
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.anchor = GridBagConstraints.WEST;
		layout.setConstraints(bgColorSelector, constraints);		
		add(bgColorSelector);
		Panel buttonPanel = new Panel(); 
		buttonPanel.setLayout(new GridLayout(1, 2));
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.EAST;
		layout.setConstraints(buttonPanel, constraints);		
		add(buttonPanel);	
		buttonPanel.add(cancelButton);
		buttonPanel.add(okButton);
	}

	public void setSettingsListener(SettingsListener listener) {
		this.listener = listener;
	}

	public void setSelection(Settings current) {
		fontSelector.setSelectedItem(current.fontName);
		fontSizeSelector.setSelectedItem(current.fontSize);
		fontColorSelector.setSelectedItem(Settings.getColorName(current.fontColor));
		bgColorSelector.setSelectedItem(Settings.getColorName(current.bgColor));
		boldChecker.setSelected(current.bold);
		italicChecker.setSelected(current.italic);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(okButton)) {
			String fontName = (String) fontSelector.getSelectedItem();
			int fontSize = (Integer) fontSizeSelector.getSelectedItem();
			Color fontColor = colorMap.get(fontColorSelector.getSelectedItem());
			Color bgColor = colorMap.get(bgColorSelector.getSelectedItem());
			boolean bold = boldChecker.isSelected();
			boolean italic = italicChecker.isSelected();
			listener.onSettingsChanged(new Settings(fontName, bold, italic, fontSize, fontColor,
					bgColor));
		}
		setVisible(false);
	}
}