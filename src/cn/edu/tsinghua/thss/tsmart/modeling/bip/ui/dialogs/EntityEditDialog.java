package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.baseline.model.refined.Entity;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

/**
 * 用于编辑实体标注的窗体
 * @author aleck
 *
 */
public class EntityEditDialog extends AbstractEditDialog {
	private static final String TEXT_INITIAL_KEYWORD 	= Messages.EntityEditDialog_0;
	private static String 		lastTimeKeyword 		= TEXT_INITIAL_KEYWORD;
			
	private static class EntityOrder implements Comparator<String> {
		private Map<String, Integer> order = new HashMap<String, Integer>();
	
		public EntityOrder(ArrayList<String> entities) {
			int v = 0;
			for (String s : entities) {
				order.put(s, v++);
			}
		}
		
		public int compare(String e1, String e2) {
			Integer v1 = order.get(e1);
			Integer v2 = order.get(e2);
			if (v1 == null) {
				throw new RuntimeException(Messages.EntityEditDialog_1 + e1);
			} else if (v2 == null) {
				throw new RuntimeException(Messages.EntityEditDialog_2 + e2);
			}
			return (v1 - v2);
		}
		
		@SuppressWarnings("unused")
        public void sort(ArrayList<String> unsorted) {
			Collections.sort(unsorted, this);
		}
		
		/**
		 * 找到e在sorted中的位置，如果不存在则返回e在sorted中应该的插入位置
		 * @Return 
		 * the index of the search key, if it is contained in the list; 
		 * otherwise, (-(insertion point) - 1). 
		 * The insertion point is defined as the point at which the key would be inserted into the list: 
		 * 		the index of the first element greater than the key, or list.size() if all elements in the list are less than the specified key. 
		 * 		Note that this guarantees that the return value will be >= 0 if and only if the key is found. 
		 * @param sorted
		 * @param e
		 */
		public int binarySearch(ArrayList<String> sorted, String key) {
			return Collections.binarySearch(sorted, key, this);
		}
	}

	private Text textKeyword;
	
	// 当前的Entities
	private ArrayList<String> currentEntities;
	// 可能的Entities
	private ArrayList<String> possibleEntities;
	// 用于返回结果的Entities
	private ArrayList<String> retEntities;
	// 记录Entity大小关系的实例
	private EntityOrder order;
	
	private List listPossibleEntities;
	private List listCurrentEntities;
	private Label lblError;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public EntityEditDialog(Shell parentShell, ArrayList<String> currentEntities) {
		super(parentShell, Messages.EntityEditDialog_3);
		this.currentEntities = removeDuplicatedEntity(new ArrayList<String>(currentEntities));
	}

	/**
	 * Modify directly on the list
	 * @param entities
	 * @return
	 */
	private ArrayList<String> removeDuplicatedEntity(ArrayList<String> entities) {
		Set<String> set = new HashSet<String>();
		int i = 0;
		while (i < entities.size()) {
			String entity = entities.get(i);
			if (set.contains(entity)) {
				entities.remove(i);
			} else {
				set.add(entity);
				i++;
			}
		}
		return entities;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (((e.stateMask & SWT.CTRL) != 0) && (e.keyCode == SWT.COMMAND)) {
					close();
				}
			}
		});
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(null);
		
		listPossibleEntities = new List(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		listPossibleEntities.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (isEnterKey(e.keyCode)) {
					addSelectedEntities();
				}
			}
		});
		listPossibleEntities.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				addSelectedEntities();
			}
		});
		listPossibleEntities.setBounds(10, 74, 271, 308);
		
		listCurrentEntities = new List(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		listCurrentEntities.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (isEnterKey(e.keyCode)) {
					removeSelectedEntities();
				}
			}
		});
		listCurrentEntities.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				removeSelectedEntities();
			}
		});
		listCurrentEntities.setBounds(399, 74, 276, 308);
		
		textKeyword = new Text(container, SWT.BORDER);
		textKeyword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				textKeyword.selectAll();
			}
		});
		textKeyword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (isEnterKey(e.keyCode)) {
					listPossibleEntities.setFocus();
				} else if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN) {
					listPossibleEntities.setFocus();
					// pass the event to the list
					keyReleased(e);
				} else {
					updateLastTimeKeyword();
					updateFilteredPossibleList();
				}
			}
		});
		textKeyword.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				textKeyword.selectAll();
			}
		});
		textKeyword.setBounds(10, 45, 233, 23);
		
		Button btnRemove = new Button(container, SWT.NONE);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeSelectedEntities();
			}
		});
		btnRemove.setBounds(295, 171, 95, 27);
		btnRemove.setText(Messages.EntityEditDialog_4);
		
		Button btnAdd = new Button(container, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addSelectedEntities();
			}
		});
		btnAdd.setText(Messages.EntityEditDialog_5);
		btnAdd.setBounds(295, 217, 95, 27);
		
		Button btnRemoveAll = new Button(container, SWT.NONE);
		btnRemoveAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				listCurrentEntities.selectAll();
				removeSelectedEntities();
			}
		});
		btnRemoveAll.setText(Messages.EntityEditDialog_6);
		btnRemoveAll.setBounds(595, 41, 80, 27);
		
		Label label = new Label(container, SWT.NONE);
		label.setFont(SWTResourceManager.getFont(Messages.EntityEditDialog_7, 12, SWT.NORMAL));
		label.setBounds(10, 10, 80, 29);
		label.setText(Messages.EntityEditDialog_8);
		
		Label label_1 = new Label(container, SWT.NONE);
		label_1.setFont(SWTResourceManager.getFont(Messages.EntityEditDialog_9, 12, SWT.NORMAL));
		label_1.setText(Messages.EntityEditDialog_10);
		label_1.setBounds(399, 39, 103, 29);
		
		lblError = new Label(container, SWT.NONE);
		lblError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblError.setBounds(438, 365, 61, 17);
		
		Button btnClearKeyword = new Button(container, SWT.FLAT | SWT.CENTER);
		btnClearKeyword.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textKeyword.setText(""); //$NON-NLS-1$
				updateLastTimeKeyword();
				updateFilteredPossibleList();
			}
		});
		btnClearKeyword.setBounds(249, 45, 32, 23);
		btnClearKeyword.setText(Messages.EntityEditDialog_12);

		initValues();
		
		return container;
	}

	protected void updateLastTimeKeyword() {
		lastTimeKeyword = textKeyword.getText();
	}

	/**
	 * 检查某一个keyCode是否是回车
	 * @param keyCode
	 * @return
	 */
	protected boolean isEnterKey(int keyCode) {
		return (keyCode == SWT.LF || keyCode == SWT.CR);
	}

	/**
	 * 将选中的项目移除
	 */
	protected void removeSelectedEntities() {
		String[] selected = listCurrentEntities.getSelection();
		for (String s : selected) {
			removeEntity(currentEntities, s);
			insertEntity(possibleEntities, s);
		}
		updateFilteredPossibleList();
		updateCurrentList();
		listPossibleEntities.setSelection(selected);
	}

	/**
	 * 加入选中的Entity
	 */
	protected void addSelectedEntities() {
		String[] selected = listPossibleEntities.getSelection();
		for (String s : selected) {
			removeEntity(possibleEntities, s);
			insertEntity(currentEntities, s);
		}
		updateFilteredPossibleList();
		updateCurrentList();
		listCurrentEntities.setSelection(selected);
	}

	/**
	 * Precondition: list is sorted
	 * @param list
	 * @param s
	 */
	protected void insertEntity(ArrayList<String> list, String s) {
		int idx = order.binarySearch(list, s);
		if (idx < 0) {
			// which is always the case, in this dialog
			list.add(-1 - idx, s);
		}
	}

	protected void removeEntity(ArrayList<String> list, String s) {
		list.remove(s);
	}

	/**
	 * Filter the possible list according to the keyword
	 */
	protected void updateFilteredPossibleList() {
		listPossibleEntities.setRedraw(false);
		listPossibleEntities.removeAll();
		String keyword = textKeyword.getText();
		if (keyword.equals(TEXT_INITIAL_KEYWORD) || keyword.isEmpty()) {
			for (String s : possibleEntities) {
				listPossibleEntities.add(s);
			}
		} else {
			keyword = keyword.toLowerCase();
			for (String s : possibleEntities) {
				if (s.toLowerCase().contains(keyword)) {
					listPossibleEntities.add(s);
				}
			}
		}
		listPossibleEntities.setRedraw(true);
	}
	
	/**
	 * 更新当前选中的Entity列表
	 */
	protected void updateCurrentList() {
		listCurrentEntities.setRedraw(false);
		listCurrentEntities.removeAll();
		for (String e : currentEntities) {
			listCurrentEntities.add(e);
		}
		listCurrentEntities.setRedraw(true);
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.EntityEditDialog_13, false);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.EntityEditDialog_14, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(691, 479);
	}

	@Override
	protected void updateValues() {
		currentEntities.clear();
		for (String s : listCurrentEntities.getItems()) {
			currentEntities.add(s);
		}
		retEntities.clear();
		retEntities.addAll(currentEntities);
	}

	@Override
	protected void initValues() {
		// 保存返回结果
		retEntities = new ArrayList<String>(currentEntities);
		// load values from the library
		possibleEntities = new ArrayList<String>();
		for (Entity entity : GlobalProperties.getInstance().getEntities()) {
			String name = entity.getName();
			possibleEntities.add(name);
		}
		possibleEntities = removeDuplicatedEntity(possibleEntities);
		order = new EntityOrder(possibleEntities);
		// FIXME: now, assume currentEntities is a subset of possibleEntities (which is very likely to hold)
		// remove already marked
		possibleEntities.removeAll(currentEntities);
		// initialize the UI
		textKeyword.setText(lastTimeKeyword);
		updateFilteredPossibleList();
		updateCurrentList();
	}

	@Override
	protected Label getErrorLabel() {
		return lblError;
	}

	@Override
	protected boolean validateUserInput() {
		// always possible
		return true;
	}
	
	protected List getListPossibleEntities() {
		return listPossibleEntities;
	}
	
	public Text getTextKeyword() {
		return textKeyword;
	}
	
	protected List getListCurrentEntities() {
		return listCurrentEntities;
	}
	
	/**
	 * Current Entity Names
	 * @return
	 */
	public ArrayList<String> getEntityNames() {
		return retEntities;
	}
}
