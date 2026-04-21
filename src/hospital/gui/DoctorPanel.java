package hospital.gui;

import hospital.model.Doctor;
import hospital.service.HospitalService;
import hospital.util.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorPanel extends JPanel {

    private final HospitalService svc;
    private JTable table; private DefaultTableModel model;
    private JTextField searchFld;
    private JTextField fId,fName,fAge,fPhone,fEmail,fSpec,fQual,fFee;
    private JComboBox<String> fGender,fDept,fAvail;

    private static final String[] COLS={
        "Doctor ID","Name","Age","Gender","Phone","Specialization","Qualification","Department","Availability","Fee"
    };

    public DoctorPanel() {
        svc=HospitalService.getInstance();
        setOpaque(false); setLayout(new BorderLayout(0,12));
        setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        buildUI(); reload(svc.getAllDoctors());
    }

    private void buildUI() {
        JPanel hdr=new JPanel(new BorderLayout()); hdr.setOpaque(false);
        hdr.add(UITheme.label("Doctor Management", UITheme.F_TITLE, UITheme.TEXT_PRIMARY), BorderLayout.WEST);
        JPanel sr=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); sr.setOpaque(false);
        searchFld=UITheme.textField(); searchFld.setPreferredSize(new Dimension(210,34));
        JButton sBt=UITheme.button("Search",UITheme.BLUE); sBt.setPreferredSize(new Dimension(88,34));
        JButton cBt=UITheme.button("Clear", UITheme.TEXT_MUTED); cBt.setPreferredSize(new Dimension(78,34));
        sr.add(UITheme.label("Search:",UITheme.F_LABEL,UITheme.TEXT_SECONDARY));
        sr.add(searchFld); sr.add(sBt); sr.add(cBt);
        hdr.add(sr, BorderLayout.EAST); add(hdr, BorderLayout.NORTH);

        JSplitPane split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(600); split.setDividerSize(6);
        split.setBorder(null); split.setOpaque(false);

        model=new DefaultTableModel(COLS,0){ @Override public boolean isCellEditable(int r,int c){return false;} };
        table=new JTable(model); UITheme.styleTable(table);
        int[] w={80,130,45,60,110,120,130,100,90,80};
        for(int i=0;i<w.length;i++) table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        JScrollPane sp=new JScrollPane(table); UITheme.styleScrollPane(sp);
        split.setLeftComponent(sp); split.setRightComponent(buildForm());
        add(split, BorderLayout.CENTER);

        JPanel bar=new JPanel(new FlowLayout(FlowLayout.CENTER,10,4)); bar.setOpaque(false);
        JButton addBt =UITheme.button("+ Add",     UITheme.GREEN);
        JButton updBt =UITheme.button("✎ Update",  UITheme.BLUE);
        JButton delBt =UITheme.button("✕ Delete",  UITheme.RED);
        JButton clrBt =UITheme.button("Clear Form",UITheme.TEXT_MUTED);
        JButton loadBt=UITheme.button("Load Row",  UITheme.GOLD);
        for(JButton b:new JButton[]{addBt,updBt,delBt,clrBt,loadBt}) b.setPreferredSize(new Dimension(130,36));
        bar.add(addBt); bar.add(updBt); bar.add(delBt); bar.add(clrBt); bar.add(loadBt);
        add(bar, BorderLayout.SOUTH);

        sBt.addActionListener(e->doSearch());
        cBt.addActionListener(e->{searchFld.setText("");reload(svc.getAllDoctors());});
        addBt.addActionListener(e->doAdd());
        updBt.addActionListener(e->doUpdate());
        delBt.addActionListener(e->doDelete());
        clrBt.addActionListener(e->clearForm());
        loadBt.addActionListener(e->loadRow());
        searchFld.addActionListener(e->doSearch());
    }

    private JPanel buildForm() {
        JPanel p=new JPanel(){ @Override protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(UITheme.BG_CARD); g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14); g2.dispose();
        }};
        p.setOpaque(false); p.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
        p.setLayout(new GridBagLayout());
        GridBagConstraints g=new GridBagConstraints();
        g.fill=GridBagConstraints.HORIZONTAL; g.insets=new Insets(4,4,4,4);

        g.gridx=0; g.gridy=0; g.gridwidth=2;
        p.add(UITheme.label("Doctor Details", UITheme.F_SUB, UITheme.GREEN), g);
        g.gridwidth=1;

        fId  =UITheme.textField(); fId.setText(svc.generateDoctorId()); fId.setEditable(true); fId.setForeground(UITheme.GREEN);
        fName=UITheme.textField(); fAge=UITheme.textField(); fPhone=UITheme.textField();
        fEmail=UITheme.textField(); fSpec=UITheme.textField(); fQual=UITheme.textField(); fFee=UITheme.textField();
        fGender=UITheme.comboBox(new String[]{"Male","Female","Other"});
        fDept  =UITheme.comboBox(new String[]{"Cardiology","Neurology","Orthopedics","Pediatrics","Surgery","Radiology","Oncology","Emergency","General"});
        fAvail =UITheme.comboBox(new String[]{"Morning","Evening","Night","Full-time"});

        Object[][] rows={{"Doctor ID *",fId},{"Full Name *",fName},{"Age",fAge},{"Gender",fGender},
                         {"Phone",fPhone},{"Email",fEmail},{"Specialization *",fSpec},{"Qualification",fQual},
                         {"Department",fDept},{"Availability",fAvail},{"Fee (Rs.)",fFee}};
        int row=1;
        for(Object[] r:rows){
            g.gridx=0; g.gridy=row; g.weightx=0.38;
            p.add(UITheme.label((String)r[0], UITheme.F_LABEL, UITheme.TEXT_SECONDARY), g);
            g.gridx=1; g.weightx=0.62; p.add((Component)r[1], g); row++;
        }
        g.gridx=0; g.gridy=row; g.gridwidth=2; g.weighty=1.0; p.add(Box.createVerticalGlue(), g);
        return p;
    }

    private void doAdd(){
        try{
            Doctor d=build();
            if(!svc.addDoctor(d)){err("Doctor ID exists!"); return;}
            reload(svc.getAllDoctors()); clearForm(); ok("Doctor added!");
        }catch(Exception e){err(e.getMessage());}
    }
    private void doUpdate(){
        try{
            Doctor d=build();
            if(!svc.updateDoctor(d)){err("Doctor not found."); return;}
            reload(svc.getAllDoctors()); ok("Doctor updated!");
        }catch(Exception e){err(e.getMessage());}
    }
    private void doDelete(){
        int row=table.getSelectedRow();
        if(row<0){err("Select a doctor."); return;}
        String id=(String)model.getValueAt(row,0), name=(String)model.getValueAt(row,1);
        if(JOptionPane.showConfirmDialog(this,"Delete Doctor "+name+" ("+id+")?",
                "Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION){
            svc.deleteDoctor(id); reload(svc.getAllDoctors()); clearForm(); ok("Deleted.");
        }
    }
    private void doSearch(){String q=searchFld.getText().trim(); reload(q.isEmpty()?svc.getAllDoctors():svc.searchDoctors(q));}
    private void loadRow(){
        int row=table.getSelectedRow(); if(row<0){err("Select a row."); return;}
        Doctor d=svc.findDoctorById((String)model.getValueAt(row,0)); if(d!=null) fill(d);
    }
    private Doctor build(){
        String id=fId.getText().trim(), name=fName.getText().trim(), spec=fSpec.getText().trim();
        if(id.isEmpty()||name.isEmpty()||spec.isEmpty()) throw new IllegalArgumentException("ID, Name, Specialization required!");
        int age=fAge.getText().trim().isEmpty()?0:Integer.parseInt(fAge.getText().trim());
        double fee=fFee.getText().trim().isEmpty()?0:Double.parseDouble(fFee.getText().trim());
        return new Doctor(id,name,age,(String)fGender.getSelectedItem(),fPhone.getText().trim(),
                fEmail.getText().trim(),spec,fQual.getText().trim(),(String)fDept.getSelectedItem(),
                (String)fAvail.getSelectedItem(),fee);
    }
    private void fill(Doctor d){
        fId.setText(d.getId()); fName.setText(d.getName()); fAge.setText(String.valueOf(d.getAge()));
        fPhone.setText(d.getPhone()); fEmail.setText(d.getEmail()); fSpec.setText(d.getSpecialization());
        fQual.setText(d.getQualification()); fFee.setText(String.valueOf(d.getConsultationFee()));
        fGender.setSelectedItem(d.getGender()); fDept.setSelectedItem(d.getDepartment());
        fAvail.setSelectedItem(d.getAvailability());
    }
    private void clearForm(){
        fId.setText(svc.generateDoctorId()); fName.setText(""); fAge.setText(""); fPhone.setText("");
        fEmail.setText(""); fSpec.setText(""); fQual.setText(""); fFee.setText("");
        fGender.setSelectedIndex(0); fDept.setSelectedIndex(0); fAvail.setSelectedIndex(0);
    }
    private void reload(List<Doctor> list){ model.setRowCount(0); list.forEach(d->model.addRow(d.toTableRow())); }
    private void ok(String m){JOptionPane.showMessageDialog(this,m,"Success",JOptionPane.INFORMATION_MESSAGE);}
    private void err(String m){JOptionPane.showMessageDialog(this,m,"Error",JOptionPane.ERROR_MESSAGE);}
}
