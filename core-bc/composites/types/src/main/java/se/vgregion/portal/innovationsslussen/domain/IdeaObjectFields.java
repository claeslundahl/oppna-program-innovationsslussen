package se.vgregion.portal.innovationsslussen.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.vgregion.portal.innovationsslussen.domain.json.ObjectField;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Patrik Bergström
 */
public class IdeaObjectFields {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdeaObjectFields.class);

    private String behov;
    private String beslutsforslag;
    private String beslutsforslagsmotivering;
    private String bilaga;
    private String epost; // e-post
    private String fodelsear;
    private String forvaltning;
    private String godkprio1;
    private String godkprio2;
    private String hsaIdKivEnhet; // HSA-ID.KIVenhet - Förvaltning
    private String ide;
    private String idetranportorensKommentar;
    private String Idetranportorenskommentarintern;
    private String instanceName; // instance.name
    private String kommavidare;
    private String komplnamn;
    private String kon;
    private String koordinatornskommentar;
    private String mobiletelephonenumber; // VGR-ID.mobiletelephonenumber
    private String prio1kommentar;
    private String prio2kommentar;
    private String prioriteringsradsmote;
    private String publik;
    private String publikintrotext;
    private String publikbeskrivning;
    private String siteLank;
    private String state;
    private String telefonnummer; // VGR-ID.hsapublictelephonenumber
    private String testat;
    private String vgrIdFullname; // VGR-ID.fullname
    private String vgrIdHsaPostalAdress; // VGR-ID.hsapostaladress
    private String vgrIdTitel; // VGR-ID.titel
    private String vgrId;
    private String vgrStrukturPerson;

    // specialFieldMappings are needed to automate population of an instance, see the populate() method
    public static final Map<String, String> specialFieldMappings = new HashMap<String, String>();

    public static final Map<String, String> specialFieldMappingsReverse = new HashMap<String, String>();

    static {
//        specialFieldMappings.put("e-post", "epost");
        specialFieldMappings.put("instance.name", "instanceName");
        specialFieldMappings.put("Idetranportorenskommentar", "idetranportorensKommentar");
        specialFieldMappings.put("VGR-ID.email", "epost");
        specialFieldMappings.put("VGR-ID.forvaltning", "forvaltning");
        specialFieldMappings.put("VGR-ID.KIVenhet", "hsaIdKivEnhet");
        specialFieldMappings.put("VGR-ID.fullname", "vgrIdFullname");
        specialFieldMappings.put("VGR-ID.hsapostaladress", "vgrIdHsaPostalAdress");
        specialFieldMappings.put("VGR-ID.hsapublictelephonenumber", "telefonnummer");
        specialFieldMappings.put("VGR-ID.mobiletelephonenumber", "mobiletelephonenumber");
        specialFieldMappings.put("VGR-ID.titel", "vgrIdTitel");
        specialFieldMappings.put("VGR-ID", "vgrId");

        for (String key: specialFieldMappings.keySet()) {
            String value = specialFieldMappings.get(key);
            specialFieldMappingsReverse.put(value, key);
        }
    }


    public void populate(List<ObjectField> objectFields) {
        for (ObjectField objectField : objectFields) {
            Field declaredField = null;
            try {
                // First we try to get the declared field directly by objectField.getId(). In the cases where
                // objectField.getId() contains characters not valid in Java variables we use the custom mappings to
                // find the field.
                declaredField = this.getClass().getDeclaredField(objectField.getId());


            } catch (NoSuchFieldException e) {
                try {
                    declaredField = this.getClass().getDeclaredField(specialFieldMappings.get(objectField.getId()));
                } catch (NoSuchFieldException e1) {

                } catch (NullPointerException e2) {

                }
            }

            if (declaredField != null) {
                declaredField.setAccessible(true);
                try {
                    declaredField.set(this, objectField.getValue());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else {
                LOGGER.warn("Couldn't find a class field for Barium field with id=" + objectField.getId());
            }
        }

        // Check whether any fields have not been set to inform the developers which can be cleaned up
        Field[] declaredFields = this.getClass().getDeclaredFields();
        for (Field df : declaredFields) {
            df.setAccessible(true);
            try {
                if (df.get(this) == null) {
                    LOGGER.info("Field \"" + df.getName() + "\" wasn't set. May it be removed from the class?");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getBehov() {
        return behov;
    }

    public void setBehov(String behov) {
        this.behov = behov;
    }

    public String getBeslutsforslag() {
        return beslutsforslag;
    }

    public void setBeslutsforslag(String beslutsforslag) {
        this.beslutsforslag = beslutsforslag;
    }

    public String getBeslutsforslagsmotivering() {
        return beslutsforslagsmotivering;
    }

    public void setBeslutsforslagsmotivering(String beslutsforslagsmotivering) {
        this.beslutsforslagsmotivering = beslutsforslagsmotivering;
    }

    public String getBilaga() {
        return bilaga;
    }

    public void setBilaga(String bilaga) {
        this.bilaga = bilaga;
    }

    public String getEpost() {
        return epost;
    }

    public void setEpost(String epost) {
        this.epost = epost;
    }

    public String getFodelsear() {
        return fodelsear;
    }

    public String getForvaltning() {
        return forvaltning;
    }

    public void setForvaltning(String forvaltning) {
        this.forvaltning = forvaltning;
    }

    public void setFodelsear(String fodelsear) {
        this.fodelsear = fodelsear;
    }

    public String getGodkprio1() {
        return godkprio1;
    }

    public void setGodkprio1(String godkprio1) {
        this.godkprio1 = godkprio1;
    }

    public String getGodkprio2() {
        return godkprio2;
    }

    public void setGodkprio2(String godkprio2) {
        this.godkprio2 = godkprio2;
    }

    public String getHsaIdKivEnhet() {
        return hsaIdKivEnhet;
    }

    public void setHsaIdKivEnhet(String hsaIdKivEnhet) {
        this.hsaIdKivEnhet = hsaIdKivEnhet;
    }

    public String getIde() {
        return ide;
    }

    public void setIde(String ide) {
        this.ide = ide;
    }

    public String getIdetranportorensKommentar() {
        return idetranportorensKommentar;
    }

    public void setIdetranportorensKommentar(String idetranportorensKommentar) {
        this.idetranportorensKommentar = idetranportorensKommentar;
    }

    public String getIdetranportorenskommentarintern() {
        return Idetranportorenskommentarintern;
    }

    public void setIdetranportorenskommentarintern(String idetranportorenskommentarintern) {
        Idetranportorenskommentarintern = idetranportorenskommentarintern;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getKommavidare() {
        return kommavidare;
    }

    public void setKommavidare(String kommavidare) {
        this.kommavidare = kommavidare;
    }

    public String getKomplnamn() {
        return komplnamn;
    }

    public void setKomplnamn(String setKomplnamn) {
        this.komplnamn = setKomplnamn;
    }

    public String getKon() {
        return kon;
    }

    public void setKon(String kon) {
        this.kon = kon;
    }

    public String getKoordinatornskommentar() {
        return koordinatornskommentar;
    }

    public void setKoordinatornskommentar(String koordinatornskommentar) {
        this.koordinatornskommentar = koordinatornskommentar;
    }

    public String getPrio1kommentar() {
        return prio1kommentar;
    }

    public void setPrio1kommentar(String prio1kommentar) {
        this.prio1kommentar = prio1kommentar;
    }

    public String getPrio2kommentar() {
        return prio2kommentar;
    }

    public void setPrio2kommentar(String prio2kommentar) {
        this.prio2kommentar = prio2kommentar;
    }

    public String getPrioriteringsradsmote() {
        return prioriteringsradsmote;
    }

    public void setPrioriteringsradsmote(String prioriteringsradsmote) {
        this.prioriteringsradsmote = prioriteringsradsmote;
    }

    public String getSiteLank() {
        return siteLank;
    }

    public void setSiteLank(String siteLank) {
        this.siteLank = siteLank;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public String getMobiletelephonenumber() {
        return mobiletelephonenumber;
    }

    public void setMobiletelephonenumber(String mobiletelephonenumber) {
        this.mobiletelephonenumber = mobiletelephonenumber;
    }


    public String getTestat() {
        return testat;
    }

    public void setTestat(String testat) {
        this.testat = testat;
    }

    public String getVgrIdFullname() {
        return vgrIdFullname;
    }

    public void setVgrIdFullname(String vgrIdFullname) {
        this.vgrIdFullname = vgrIdFullname;
    }

    public String getVgrIdHsaPostalAdress() {
        return vgrIdHsaPostalAdress;
    }

    public void setVgrIdHsaPostalAdress(String vgrIdHsaPostalAdress) {
        this.vgrIdHsaPostalAdress = vgrIdHsaPostalAdress;
    }

    public String getVgrIdTitel() {
        return vgrIdTitel;
    }

    public void setVgrIdTitel(String vgrIdTitel) {
        this.vgrIdTitel = vgrIdTitel;
    }

    public void setVgrId(String vgrId) {
        this.vgrId = vgrId;
    }

    public String getVgrId() {
        return vgrId;
    }

    public String getPublikintrotext() {
        return publikintrotext;
    }

    public void setPublikintrotext(String publikintrotext) {
        this.publikintrotext = publikintrotext;
    }

    public String getPublikbeskrivning() {
        return publikbeskrivning;
    }

    public void setPublikbeskrivning(String publikbeskrivning) {
        this.publikbeskrivning = publikbeskrivning;
    }

    public String getPublik() {
        return publik;
    }

    public void setPublik(String publik) {
        this.publik = publik;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getVgrStrukturPerson() {
        return vgrStrukturPerson;
    }

    public void setVgrStrukturPerson(String vgrStrukturPerson) {
        this.vgrStrukturPerson = vgrStrukturPerson;
    }
}
