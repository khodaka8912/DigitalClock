package gui01_02;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
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

	private SettingsListener listener;

	public SettingDialog(Frame owner) {
		super(owner, Consts.Strings.SETTINGS, true);

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

		Button okButton = new Button("OK");
		okButton.addActionListener(this);

		// Layout
		setLayout(new GridLayout(6, 2));
		setSize(350, 250);
		setResizable(false);

		// Add UI elements
		add(new Label(Consts.Strings.FONT));
		add(fontSelector);
		add(boldChecker);
		add(italicChecker);
		add(new Label(Consts.Strings.FONT_SIZE));
		add(fontSizeSelector);
		add(new Label(Consts.Strings.FONT_COLOR));
		add(fontColorSelector);
		add(new Label(Consts.Strings.BG_COLOR));
		add(bgColorSelector);
		add(new Label(""));
		add(okButton);
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
		italicChecker.setState(current.itaric);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String fontName = fontSelector.getSelectedItem();
		int fontSize = Integer.parseInt(fontSizeSelector.getSelectedItem());
		Color fontColor = colorMap.get(fontColorSelector.getSelectedItem());
		Color bgColor = colorMap.get(bgColorSelector.getSelectedItem());
		boolean bold = boldChecker.getState();
		boolean italic = italicChecker.getState();
		listener.onSettingsChanged(new Settings(fontName, bold, italic, fontSize, fontColor, bgColor));
		setVisible(false);
	}

}