/*
 *  ============================================================================================
 *  A2.java : Meera Kabilan
 *  YOUR UPI: mkab129
 *  This programme displays a JTree and JTable of different shapes
 *  ============================================================================================
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import javax.swing.table.*;
import java.io.*;
import javax.swing.tree.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

public class A2  extends JFrame {
    private NestedShape root = new NestedShape(Shape.DEFAULT_PANEL_WIDTH, Shape.DEFAULT_PANEL_HEIGHT);
    private NestedShape selectedShape = root;
    private CustomDataModel dataModel = new CustomDataModel();
    JComboBox<ShapeType> shapesComboBox;
    JButton addNodeButton, removeNodeButton;
    JTextField xTextField, yTextField, widthTextField, heightTextField;
    JTree tree;
    JTable shapesTable;

    public A2() {
        super("A2");
        JPanel mainPanel = setUpMainPanel();
        JPanel toolsPanel = setUpToolsPanel();
        add(mainPanel, BorderLayout.CENTER);
        add(toolsPanel, BorderLayout.NORTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
    public JPanel setUpMainPanel() {
        JPanel mainPanel = new JPanel();
        tree = new JTree(dataModel);
        tree.addTreeSelectionListener(new SelectListener());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        JPanel treePanel = new JPanel(new BorderLayout());
        JScrollPane treeScrollpane = new JScrollPane(tree);
        JPanel treeButtonsPanel = new JPanel();
        addNodeButton = new JButton("Add Node");
        addNodeButton.addActionListener( new AddListener());
        removeNodeButton = new JButton("Remove Node");
        removeNodeButton.addActionListener( new RemoveListener());
        treeButtonsPanel.add(addNodeButton);
        treeButtonsPanel.add(removeNodeButton);
        treePanel.add(treeButtonsPanel,BorderLayout.NORTH);
        treePanel.add(treeScrollpane,BorderLayout.CENTER);

        shapesTable = new JTable(dataModel);
        treePanel.setPreferredSize(new Dimension(Shape.DEFAULT_PANEL_WIDTH/2, Shape.DEFAULT_PANEL_HEIGHT));
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, new JScrollPane(shapesTable));
        mainSplitPane.setResizeWeight(0.5);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setContinuousLayout(true);
        mainPanel.add(mainSplitPane);
        return mainPanel;
    }
    public JPanel setUpToolsPanel() {
        shapesComboBox = new JComboBox<ShapeType>(new DefaultComboBoxModel<ShapeType>(ShapeType.values()));
        JPanel toolsPanel = new JPanel();
        toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.X_AXIS));
        toolsPanel.add(new JLabel(" Shape: ", JLabel.RIGHT));
        toolsPanel.add(shapesComboBox);
        toolsPanel.add(new JLabel("x: ", JLabel.RIGHT));
        xTextField = new JTextField("0", 10);
        xTextField.setToolTipText("Max:" + selectedShape.width);
        toolsPanel.add(xTextField);
        toolsPanel.add(new JLabel("y: ", JLabel.RIGHT));
        yTextField = new JTextField("0", 10);
        yTextField.setToolTipText("Max:" + selectedShape.height);
        toolsPanel.add(yTextField);
        toolsPanel.add(new JLabel("width: ", JLabel.RIGHT));
        widthTextField = new JTextField("" + selectedShape.width, 10);
        widthTextField.setToolTipText("Max:" + selectedShape.width);
        toolsPanel.add(widthTextField);
        toolsPanel.add(new JLabel("height: ", JLabel.RIGHT));
        heightTextField = new JTextField("" + selectedShape.height, 10);
        heightTextField.setToolTipText("Max:" + selectedShape.height);
        toolsPanel.add(heightTextField);
        xTextField.addActionListener(new XPositionListener());
        yTextField.addActionListener(new YPositionListener());
        widthTextField.addActionListener(new WidthListener());
        heightTextField.addActionListener(new HeightListener());
        return toolsPanel;
    }
    class CustomDataModel extends AbstractTableModel implements TreeModel {
        private ArrayList<TreeModelListener> treeModelListeners = new ArrayList<TreeModelListener>();
        CustomDataModel() {}
        public NestedShape getRoot() {return root;}
        public boolean isLeaf(Object node) {
            if ((node instanceof Shape) && !(node instanceof NestedShape))
                return true;
            return false;
        }
        public boolean isRoot(Shape selectedNode) {
            return selectedNode == root;
        }
        public Shape getChild(Object parent, int index) {
            if (index < 0 || index > ((NestedShape)parent).getSize() || !(parent instanceof NestedShape))
                return null;
            return ((NestedShape)parent).getInnerShapeAt(index);
        }
        public int getChildCount(Object parent) {
            if (!(parent instanceof NestedShape))
                return 0;
            return ((NestedShape)parent).getSize();
        }
        public int getIndexOfChild(Object parent, Object child) {
            if (!(parent instanceof NestedShape) || !(child instanceof Shape))
                return -1;
            return ((NestedShape)parent).indexOf((Shape)child);
        }
        public void addTreeModelListener(final TreeModelListener tml) {
            treeModelListeners.add(tml);
        }
        public void removeTreeModelListener(final TreeModelListener tml) {
            treeModelListeners.remove(tml);
        }
        public void valueForPathChanged(TreePath path, Object newValue) {;}
        private String[] columnNames = new String[]{"type", "x", "y", "width", "height", "area"};
        public int getColumnCount() {return columnNames.length;}
        public int getRowCount() {
            return selectedShape.getSize();
        }
        public String getColumnName(int colIndex) {
            return columnNames[colIndex];
        }
        public Object getValueAt(int rowIndex, int colIndex) {
            Shape innerShape = selectedShape.getInnerShapeAt(rowIndex);
            switch (colIndex) {
                case 0:
                    return innerShape.getClass().getName();
                case 1:
                    return innerShape.x;
                case 2:
                    return innerShape.y;
                case 3:
                    return innerShape.width;
                case 4:
                    return innerShape.height;
                case 5:
                    return innerShape.getArea();
                default:
                    return null;
            }
        }
        public void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children) {
            TreeModelEvent event = new TreeModelEvent(source, path, childIndices, children);
            for (int i = 0; i < treeModelListeners.size(); i++)
                treeModelListeners.get(i).treeNodesInserted(event);
            System.out.printf("Called fireTreeNodesInserted: path=%s, childIndices=%s, children=%s\n",
                    Arrays.toString(path), Arrays.toString(childIndices), Arrays.toString(children));
        }
        public void insertNodeInto(NestedShape selectedNode, ShapeType shapeType, int x, int y, int w, int h) {
            fireTreeNodesInserted(this, selectedNode.getPath(), new int[]{selectedNode.getSize()},
                    new Object[]{selectedNode.createInnerShape(shapeType, x, y, w, h)});
            fireTableRowsInserted(selectedNode.getSize()-1, selectedNode.getSize()-1);
        }
        public void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children) {
            TreeModelEvent event = new TreeModelEvent(source, path, childIndices, children);
            for (int i = 0; i < treeModelListeners.size(); i++)
                treeModelListeners.get(i).treeNodesRemoved(event);
            System.out.printf("Called fireTreeNodesRemoved: path=%s, childIndices=%s, children=%s\n",
                    Arrays.toString(path), Arrays.toString(childIndices), Arrays.toString(children));
        }
        public void removeNodeFromParent(Shape selectedNode) {
            NestedShape parent = selectedNode.getParent();
            int i = parent.indexOf(selectedNode);
            parent.removeInnerShape(selectedNode);
            fireTreeNodesRemoved(this, parent.getPath(), new int[]{i}, new Object[]{selectedNode});
            fireTableRowsDeleted(i, i);
        }
    }

    class AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object selected = tree.getLastSelectedPathComponent();
            if (selected instanceof NestedShape) {
                try {
                    dataModel.insertNodeInto(((NestedShape)selected), (ShapeType)shapesComboBox.getSelectedItem(),
                            Integer.parseInt(xTextField.getText()), Integer.parseInt(yTextField.getText()),
                            Integer.parseInt(widthTextField.getText()), Integer.parseInt(heightTextField.getText()));
                }
                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR: Invalid values!");
                }
            }
            else if (selected == null)
                JOptionPane.showMessageDialog(null, "ERROR: No node selected.");
            else
                JOptionPane.showMessageDialog(null, "ERROR: Must select a NestedShape node.");
        }
    }

    class RemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object selected = tree.getLastSelectedPathComponent();
            if (selected == root)
                JOptionPane.showMessageDialog(null, "ERROR: Must not remove the root.");
            else if (selected == null)
                JOptionPane.showMessageDialog(null, "ERROR: No node selected.");
            else
                dataModel.removeNodeFromParent((Shape)selected);
        }
    }


    class SelectListener implements TreeSelectionListener {
        public void valueChanged(TreeSelectionEvent e) {
            Object selected = tree.getLastSelectedPathComponent();
            if (selected instanceof NestedShape) {
                selectedShape = (NestedShape)selected;
                dataModel.fireTableDataChanged();
                tree.expandPath(e.getPath());
                int shapeWidth = selectedShape.width;
                int shapeHeight = selectedShape.height;
                xTextField.setText("0");
                yTextField.setText("0");
                widthTextField.setText(String.valueOf(shapeWidth / 2));
                heightTextField.setText(String.valueOf(shapeHeight / 2));
                xTextField.setToolTipText("Max:" + shapeWidth);
                yTextField.setToolTipText("Max:" + shapeHeight);
                widthTextField.setToolTipText("Max:" + shapeWidth);
                heightTextField.setToolTipText("Max:" + shapeHeight);
            }
        }
    }
    class XPositionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int xText = Integer.parseInt(xTextField.getText());
                if (xText < 0 || xText > selectedShape.width)
                    xTextField.setText("0");
            }
            catch (NumberFormatException ex) {
                xTextField.setText("0");
            }
        }
    }
    class YPositionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int yText = Integer.parseInt(yTextField.getText());
                if (yText < 0 || yText > selectedShape.height)
                    yTextField.setText("0");
            }
            catch (NumberFormatException ex) {
                yTextField.setText("0");
            }
        }
    }
    class WidthListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int wText = Integer.parseInt(widthTextField.getText());
                if (wText < 0 || wText > selectedShape.width) {
                    String w = String.format("%d", selectedShape.width / 2);
                    widthTextField.setText(w);
                }
            }
            catch (NumberFormatException ex) {
                String w = String.format("%d", selectedShape.width / 2);
                widthTextField.setText(w);
            }
        }
    }
    class HeightListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                int hText = Integer.parseInt(heightTextField.getText());
                if (hText < 0 || hText > selectedShape.height) {
                    String h = String.format("%d", selectedShape.height / 2);
                    heightTextField.setText(h);
                }
            }
            catch (NumberFormatException ex) {
                String h = String.format("%d", selectedShape.height / 2);
                heightTextField.setText(h);
            }
        }
    }


    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new A2();
            }
        });
    }
}