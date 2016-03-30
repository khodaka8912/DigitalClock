package gui02_02;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
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

		boldChecker = new JCheckBox(Consts.Strings.BOLD);
		italicChecker = new JCheckBox(Consts.Strings.ITALIC);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(this);

		setLayout(new GridLayout(6, 2));
		setSize(350, 250);
		setResizable(false);

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
		fontSelector.setSelectedItem(current.fontName);
		fontSizeSelector.setSelectedItem(current.fontSize);
		fontColorSelector.setSelectedItem(Settings.getColorName(current.fontColor));
		bgColorSelector.setSelectedItem(Settings.getColorName(current.bgColor));
		boldChecker.setSelected(current.bold);
		italicChecker.setSelected(current.itaric);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String fontName = (String) fontSelector.getSelectedItem();
		int fontSize = (Integer) fontSizeSelector.getSelectedItem();
		Color fontColor = colorMap.get(fontColorSelector.getSelectedItem());
		Color bgColor = colorMap.get(bgColorSelector.getSelectedItem());
		boolean bold = boldChecker.isSelected();
		boolean italic = italicChecker.isSelected();
		listener.onSettingsChanged(
				new Settings(fontName, bold, italic, fontSize, fontColor, bgColor));
		setVisible(false);
	}

	// private class ColorPicker extends JLabel implements ListCellRenderer {
	//
	// ColorPicker() {
	// setOpaque(true);
	// }
	//
	// public Component getListCellRendererComponent(JList list, Object value,
	// int index,
	// boolean isSelected, boolean cellHasFocus) {
	//
	// String data = (String) value;
	// setText(data);
	// setIcon(new ImageIcon());
	//
	// if (isSelected) {
	// setForeground(Color.white);
	// setBackground(Color.black);
	// } else {
	// setForeground(Color.black);
	// setBackground(Color.white);
	// }
	//
	// return this;
	// }
	// }
}