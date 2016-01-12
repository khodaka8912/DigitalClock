package gui01_04;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;

public class SettingDialog extends Dialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final List<Integer> fontSizes = Settings.getFontSizeList();
	private final List<String> fonts = Settings.getFontList();
	private final Map<String, Color> colorMap = Settings.getColorMap();

	private Choice fontSelector;
	private Checkbox boldChecker;
	private Checkbox italicChecker;
	private Choice fontSizeSelector;
	private Choice fontColorSelector;
	private Choice bgColorSelector;
	private Button okButton;
	private Button cancelButton;

	private SettingsListener listener;

	public SettingDialog(Window owner) {
		super(owner, Consts.Strings.SETTINGS, ModalityType.APPLICATION_MODAL);
		init();
	}

	public SettingDialog(Frame owner) {
		super(owner, Consts.Strings.SETTINGS, true);
		init();
	}

	private void init() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});

		fontSelector = new Choice();
		fontSizeSelector = new Choice();
		fontColorSelector = new Choice();
		bgColorSelector = new Choice();

		fontSizes.stream().forEach((s) -> fontSizeSelector.add(s.toString()));
		fonts.stream().forEach((s) -> fontSelector.add(s));
		colorMap.keySet().stream().forEach((s) -> {
			fontColorSelector.add(s);
			bgColorSelector.add(s);
		});

		boldChecker = new Checkbox("Bold");
		italicChecker = new Checkbox("Italic");

		okButton = new Button("OK");
		cancelButton = new Button("Cancel");
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		// Layout
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		setSize(350, 220);
		setResizable(false);

		// Add UI elements
		Label fontLabel = new Label(Consts.Strings.FONT);
		Label sizeLabel = new Label(Consts.Strings.FONT_SIZE);
		Label fontColorLabel = new Label(Consts.Strings.FONT_COLOR);
		Label bgColorLabel = new Label(Consts.Strings.BG_COLOR);
		
		GridBagConstraints constraints = new GridBagConstraints();
		
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
		fontSelector.select(current.fontName);
		fontSizeSelector.select(Integer.toString(current.fontSize));
		fontColorSelector.select(Settings.getColorName(current.fontColor));
		bgColorSelector.select(Settings.getColorName(current.bgColor));
		boldChecker.setState(current.bold);
		italicChecker.setState(current.italic);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(okButton)) {
		String fontName = fontSelector.getSelectedItem();
		int fontSize = Integer.parseInt(fontSizeSelector.getSelectedItem());
		Color fontColor = colorMap.get(fontColorSelector.getSelectedItem());
		Color bgColor = colorMap.get(bgColorSelector.getSelectedItem());
		boolean bold = boldChecker.getState();
		boolean italic = italicChecker.getState();
		listener.onSettingsChanged(new Settings(fontName, bold, italic, fontSize, fontColor, bgColor));
		}
		setVisible(false);
	}

}