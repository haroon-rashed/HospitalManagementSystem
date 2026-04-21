package hospital.gui;

import hospital.model.Patient;
import hospital.service.HospitalService;
import hospital.util.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientPanel extends JPanel {

    private final HospitalService svc;
    private JTable table; private DefaultTableModel model;
    private JTextField searchFld;
    private JTextField fId,fName,fAge,fPhone,fEmail,fBlood,fDiag,fDate,fDocId;
    private JComboBox<String> fGender,fWard,fStatus;

    private static final String[] COLS = {
        "Patient ID","Name","Age","Gender","Phone","Blood","Diagnosis","Ward","Status"
    };

    public PatientPanel() {
        svc = HospitalService.getInstance();
        setOpaque(false);
        setLayout(new BorderLayout(0,12));
        setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        buildUI();
        reload(svc.getAllPatients());
    }

    private void buildUI() {
        // Header
        JPanel hdr=new JPanel(new BorderLayout()); hdr.setOpaque(false);
        hdr.add(UITheme.label("Patient Management", UITheme.F_TITLE, UITheme.TEXT_PRIMARY), BorderLayout.WEST);
        JPanel sr=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); sr.setOpaque(false);
        searchFld=UITheme.textField(); searchFld.setPreferredSize(new Dimension(210,34));
        JButton sBt=UITheme.button("Search", UITheme.BLUE); sBt.setPreferredSize(new Dimension(88,34));
        JButton cBt=UITheme.button("Clear",  UITheme.TEXT_MUTED); cBt.setPreferredSize(new Dimension(78,34));
        sr.add(UITheme.label("Search:", UITheme.F_LABEL, UITheme.TEXT_SECONDARY));
        sr.add(searchFld); sr.add(sBt); sr.add(cBt);
        hdr.add(sr, BorderLayout.EAST);
        add(hdr, BorderLayout.NORTH);

        // Split
        JSplitPane split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(600); split.setDividerSize(6);
        split.setBorder(null); split.setOpaque(false);

        model=new DefaultTableModel(COLS,0){ @Override public boolean isCellEditable(int r,int c){return false;} };
        table=new JTable(model); UITheme.styleTable(table);
        int[] w={80,130,45,65,110,70,130,80,80};
        for(int i=0;i<w.length;i++) table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        JScrollPane sp=new JScrollPane(table); UITheme.styleScrollPane(sp);
        split.setLeftComponent(sp);
        split.setRightComponent(buildForm());
        add(split, BorderLayout.CENTER);

        // Buttons
        JPanel bar=new JPanel(new FlowLayout(FlowLayout.CENTER,10,4)); bar.setOpaque(false);
        JButton addBt =UITheme.button("+ Add",      UITheme.GREEN);
        JButton updBt =UITheme.button("✎ Update",   UITheme.BLUE);
        JButton delBt =UITheme.button("✕ Delete",   UITheme.RED);
        JButton clrBt =UITheme.button("Clear Form", UITheme.TEXT_MUTED);
        JButton loadBt=UITheme.button("Load Row",   UITheme.GOLD);
        for(JButton b:new JButton[]{addBt,updBt,delBt,clrBt,loadBt})
            b.setPreferredSize(new Dimension(130,36));
        bar.add(addBt); bar.add(updBt); bar.add(delBt); bar.add(clrBt); bar.add(loadBt);
        add(bar, BorderLayout.SOUTH);

        sBt.addActionListener(e->doSearch());
        cBt.addActionListener(e->{searchFld.setText("");reload(svc.getAllPatients());});
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
        p.add(UITheme.label("Patient Details", UITheme.F_SUB, UITheme.GOLD), g);
        g.gridwidth=1;

        fId   =UITheme.textField(); fId.setText(svc.generatePatientId()); fId.setEditable(true); fId.setForeground(UITheme.GOLD);
        fName =UITheme.textField(); fAge=UITheme.textField(); fPhone=UITheme.textField();
        fEmail=UITheme.textField(); fBlood=UITheme.textField(); fDiag=UITheme.textField();
        fDate =UITheme.textField(); fDocId=UITheme.textField();
        fGender=UITheme.comboBox(new String[]{"Male","Female","Other"});
        fWard  =UITheme.comboBox(new String[]{"Ward A","Ward B","Ward C","Pediatric","Surgical","ICU","OPD"});
        fStatus=UITheme.comboBox(new String[]{"Admitted","OPD","Discharged","ICU"});

        Object[][] rows={{"Patient ID *",fId},{"Full Name *",fName},{"Age *",fAge},{"Gender",fGender},
                         {"Phone *",fPhone},{"Email",fEmail},{"Blood Group",fBlood},{"Diagnosis",fDiag},
                         {"Admission Date",fDate},{"Ward",fWard},{"Doctor ID",fDocId},{"Status",fStatus}};
        int row=1;
        for(Object[] r:rows){
            g.gridx=0; g.gridy=row; g.weightx=0.35;
            p.add(UITheme.label((String)r[0], UITheme.F_LABEL, UITheme.TEXT_SECONDARY), g);
            g.gridx=1; g.weightx=0.65; p.add((Component)r[1], g); row++;
        }
        g.gridx=0; g.gridy=row; g.gridwidth=2; g.weighty=1.0;
        p.add(Box.createVerticalGlue(), g);
        return p;
    }

    private void doAdd() {
        try {
            Patient pt=build();
            if(!svc.addPatient(pt)){err("Patient ID already exists!"); return;}
            reload(svc.getAllPatients()); clearForm();
            ok("Patient added successfully!");
        } catch(Exception e){err(e.getMessage());}
    }
    private void doUpdate() {
        try {
            Patient pt=build();
            if(!svc.updatePatient(pt)){err("Patient not found."); return;}
            reload(svc.getAllPatients()); ok("Patient updated!");
        } catch(Exception e){err(e.getMessage());}
    }
    private void doDelete() {
        int row=table.getSelectedRow();
        if(row<0){err("Select a patient to delete."); return;}
        String id=(String)model.getValueAt(row,0), name=(String)model.getValueAt(row,1);
        if(JOptionPane.showConfirmDialog(this,"Delete patient: "+name+" ("+id+")?",
                "Confirm",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION){
            svc.deletePatient(id); reload(svc.getAllPatients()); clearForm(); ok("Deleted.");
        }
    }
    private void doSearch() {
        String q=searchFld.getText().trim();
        reload(q.isEmpty()?svc.getAllPatients():svc.searchPatients(q));
    }
    private void loadRow() {
        int row=table.getSelectedRow();
        if(row<0){err("Select a row first."); return;}
        Patient p=svc.findPatientById((String)model.getValueAt(row,0));
        if(p!=null) fill(p);
    }
    private Patient build() {
        String id=fId.getText().trim(), name=fName.getText().trim(),
               ageS=fAge.getText().trim(), phone=fPhone.getText().trim();
        if(id.isEmpty()||name.isEmpty()||ageS.isEmpty()||phone.isEmpty())
            throw new IllegalArgumentException("ID, Name, Age, Phone are required!");
        int age=Integer.parseInt(ageS);
        if(age<0||age>150) throw new IllegalArgumentException("Invalid age!");
        return new Patient(id,name,age,(String)fGender.getSelectedItem(),phone,
                fEmail.getText().trim(),
                fBlood.getText().trim().isEmpty()?"Unknown":fBlood.getText().trim(),
                fDiag.getText().trim().isEmpty()?"Pending":fDiag.getText().trim(),
                fDate.getText().trim(),(String)fWard.getSelectedItem(),
                fDocId.getText().trim(),(String)fStatus.getSelectedItem());
    }
    private void fill(Patient p) {
        fId.setText(p.getId()); fName.setText(p.getName()); fAge.setText(String.valueOf(p.getAge()));
        fPhone.setText(p.getPhone()); fEmail.setText(p.getEmail()); fBlood.setText(p.getBloodGroup());
        fDiag.setText(p.getDiagnosis()); fDate.setText(p.getAdmissionDate());
        fDocId.setText(p.getAssignedDoctorId());
        fGender.setSelectedItem(p.getGender()); fWard.setSelectedItem(p.getWard());
        fStatus.setSelectedItem(p.getStatus());
    }
    private void clearForm() {
        fId.setText(svc.generatePatientId()); fName.setText(""); fAge.setText("");
        fPhone.setText(""); fEmail.setText(""); fBlood.setText(""); fDiag.setText("");
        fDate.setText(""); fDocId.setText("");
        fGender.setSelectedIndex(0); fWard.setSelectedIndex(0); fStatus.setSelectedIndex(0);
    }
    private void reload(List<Patient> list){
        model.setRowCount(0); list.forEach(p->model.addRow(p.toTableRow()));
    }
    private void ok(String m){JOptionPane.showMessageDialog(this,m,"Success",JOptionPane.INFORMATION_MESSAGE);}
    private void err(String m){JOptionPane.showMessageDialog(this,m,"Error",JOptionPane.ERROR_MESSAGE);}
}
