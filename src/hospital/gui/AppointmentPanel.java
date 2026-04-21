package hospital.gui;

import hospital.model.Appointment;
import hospital.service.HospitalService;
import hospital.util.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppointmentPanel extends JPanel {

    private final HospitalService svc;
    private JTable table; private DefaultTableModel model;
    private JTextField searchFld;
    private JTextField fId,fPid,fPname,fDid,fDname,fDate,fTime,fReason,fNotes;
    private JComboBox<String> fStatus;

    private static final String[] COLS={"Appt ID","Patient Name","Doctor Name","Date","Time","Reason","Status"};

    public AppointmentPanel(){
        svc=HospitalService.getInstance();
        setOpaque(false); setLayout(new BorderLayout(0,12));
        setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        buildUI(); reload(svc.getAllAppointments());
    }

    private void buildUI(){
        JPanel hdr=new JPanel(new BorderLayout()); hdr.setOpaque(false);
        hdr.add(UITheme.label("Appointment Management", UITheme.F_TITLE, UITheme.TEXT_PRIMARY), BorderLayout.WEST);
        JPanel sr=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); sr.setOpaque(false);
        searchFld=UITheme.textField(); searchFld.setPreferredSize(new Dimension(210,34));
        JButton sBt=UITheme.button("Search",UITheme.BLUE); sBt.setPreferredSize(new Dimension(88,34));
        JButton cBt=UITheme.button("Clear", UITheme.TEXT_MUTED); cBt.setPreferredSize(new Dimension(78,34));
        sr.add(UITheme.label("Search:",UITheme.F_LABEL,UITheme.TEXT_SECONDARY));
        sr.add(searchFld); sr.add(sBt); sr.add(cBt);
        hdr.add(sr, BorderLayout.EAST); add(hdr, BorderLayout.NORTH);

        JSplitPane split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(570); split.setDividerSize(6);
        split.setBorder(null); split.setOpaque(false);

        model=new DefaultTableModel(COLS,0){ @Override public boolean isCellEditable(int r,int c){return false;} };
        table=new JTable(model); UITheme.styleTable(table);
        JScrollPane sp=new JScrollPane(table); UITheme.styleScrollPane(sp);
        split.setLeftComponent(sp); split.setRightComponent(buildForm());
        add(split, BorderLayout.CENTER);

        JPanel bar=new JPanel(new FlowLayout(FlowLayout.CENTER,10,4)); bar.setOpaque(false);
        JButton addBt =UITheme.button("+ Book",    UITheme.GREEN);
        JButton updBt =UITheme.button("✎ Update",  UITheme.BLUE);
        JButton delBt =UITheme.button("✕ Cancel",  UITheme.RED);
        JButton clrBt =UITheme.button("Clear Form",UITheme.TEXT_MUTED);
        JButton loadBt=UITheme.button("Load Row",  UITheme.GOLD);
        for(JButton b:new JButton[]{addBt,updBt,delBt,clrBt,loadBt}) b.setPreferredSize(new Dimension(130,36));
        bar.add(addBt); bar.add(updBt); bar.add(delBt); bar.add(clrBt); bar.add(loadBt);
        add(bar, BorderLayout.SOUTH);

        sBt.addActionListener(e->doSearch());
        cBt.addActionListener(e->{searchFld.setText("");reload(svc.getAllAppointments());});
        addBt.addActionListener(e->doAdd());
        updBt.addActionListener(e->doUpdate());
        delBt.addActionListener(e->doDelete());
        clrBt.addActionListener(e->clearForm());
        loadBt.addActionListener(e->loadRow());
        searchFld.addActionListener(e->doSearch());
    }

    private JPanel buildForm(){
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
        p.add(UITheme.label("Appointment Details", UITheme.F_SUB, UITheme.BLUE), g);
        g.gridwidth=1;

        fId    =UITheme.textField(); fId.setText(svc.generateAppointmentId()); fId.setEditable(true); fId.setForeground(UITheme.BLUE);
        fPid   =UITheme.textField(); fPname=UITheme.textField(); fDid=UITheme.textField();
        fDname =UITheme.textField(); fDate=UITheme.textField(); fTime=UITheme.textField();
        fReason=UITheme.textField(); fNotes=UITheme.textField();
        fStatus=UITheme.comboBox(new String[]{"Scheduled","Completed","Cancelled","No Show"});

        Object[][] rows={{"Appt ID *",fId},{"Patient ID *",fPid},{"Patient Name *",fPname},
                         {"Doctor ID *",fDid},{"Doctor Name *",fDname},
                         {"Date * (YYYY-MM-DD)",fDate},{"Time * (e.g. 10:00 AM)",fTime},
                         {"Reason",fReason},{"Notes",fNotes},{"Status",fStatus}};
        int row=1;
        for(Object[] r:rows){
            g.gridx=0; g.gridy=row; g.weightx=0.4;
            p.add(UITheme.label((String)r[0], UITheme.F_LABEL, UITheme.TEXT_SECONDARY), g);
            g.gridx=1; g.weightx=0.6; p.add((Component)r[1], g); row++;
        }
        g.gridx=0; g.gridy=row; g.gridwidth=2; g.weighty=1.0; p.add(Box.createVerticalGlue(), g);
        return p;
    }

    private void doAdd(){
        try{
            Appointment a=build(); svc.addAppointment(a);
            reload(svc.getAllAppointments()); clearForm(); ok("Appointment booked!");
        }catch(Exception e){err(e.getMessage());}
    }
    private void doUpdate(){
        try{
            Appointment a=build();
            if(!svc.updateAppointment(a)){err("Appointment not found."); return;}
            reload(svc.getAllAppointments()); ok("Updated!");
        }catch(Exception e){err(e.getMessage());}
    }
    private void doDelete(){
        int row=table.getSelectedRow(); if(row<0){err("Select an appointment."); return;}
        String id=(String)model.getValueAt(row,0);
        if(JOptionPane.showConfirmDialog(this,"Cancel appointment "+id+"?","Confirm",
                JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION){
            svc.deleteAppointment(id); reload(svc.getAllAppointments()); clearForm(); ok("Cancelled.");
        }
    }
    private void doSearch(){String q=searchFld.getText().trim(); reload(q.isEmpty()?svc.getAllAppointments():svc.searchAppointments(q));}
    private void loadRow(){
        int row=table.getSelectedRow(); if(row<0){err("Select a row."); return;}
        Appointment a=svc.findAppointmentById((String)model.getValueAt(row,0));
        if(a!=null){
            fId.setText(a.getAppointmentId()); fPid.setText(a.getPatientId()); fPname.setText(a.getPatientName());
            fDid.setText(a.getDoctorId()); fDname.setText(a.getDoctorName()); fDate.setText(a.getDate());
            fTime.setText(a.getTime()); fReason.setText(a.getReason()); fNotes.setText(a.getNotes());
            fStatus.setSelectedItem(a.getStatus());
        }
    }
    private Appointment build(){
        String id=fId.getText().trim(),pid=fPid.getText().trim(),pn=fPname.getText().trim(),
               did=fDid.getText().trim(),dn=fDname.getText().trim(),dt=fDate.getText().trim(),tm=fTime.getText().trim();
        if(id.isEmpty()||pid.isEmpty()||pn.isEmpty()||did.isEmpty()||dn.isEmpty()||dt.isEmpty()||tm.isEmpty())
            throw new IllegalArgumentException("All starred fields are required!");
        Appointment a=new Appointment(id,pid,pn,did,dn,dt,tm,fReason.getText().trim(),(String)fStatus.getSelectedItem());
        a.setNotes(fNotes.getText().trim()); return a;
    }
    private void clearForm(){
        fId.setText(svc.generateAppointmentId()); fPid.setText(""); fPname.setText(""); fDid.setText("");
        fDname.setText(""); fDate.setText(""); fTime.setText(""); fReason.setText(""); fNotes.setText("");
        fStatus.setSelectedIndex(0);
    }
    private void reload(List<Appointment> list){ model.setRowCount(0); list.forEach(a->model.addRow(a.toTableRow())); }
    private void ok(String m){JOptionPane.showMessageDialog(this,m,"Success",JOptionPane.INFORMATION_MESSAGE);}
    private void err(String m){JOptionPane.showMessageDialog(this,m,"Error",JOptionPane.ERROR_MESSAGE);}
}
