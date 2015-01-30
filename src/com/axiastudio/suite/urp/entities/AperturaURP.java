package com.axiastudio.suite.urp.entities;

import javax.persistence.*;

/**
 * User: tiziano
 * Date: 20/01/15
 * Time: 08:38
 */
@Entity
@Table(schema="urp")
@SequenceGenerator(name="genaperturaurp", sequenceName="urp.aperturaurp_id_seq", initialValue=1, allocationSize=1)
public class AperturaURP {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genaperturaurp")
    private Long id;

    @Column(name="giornosettimana")
    @Enumerated(EnumType.ORDINAL)
    private GiornoSettimana giornosettimana;

    @Column(name="anticipoapertura")
    private Integer anticipoapertura;

    @Column(name="oraapertura")
    private Integer oraapertura;

    @Column(name="minutoapertura")
    private Integer minutoapertura;

    @Column(name="orachiusura")
    private Integer orachiusura;

    @Column(name="minutochiusura")
    private Integer minutochiusura;

    @Column(name="anticipochiusura")
    private Integer anticipochiusura;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GiornoSettimana getGiornosettimana() {
        return giornosettimana;
    }

    public void setGiornosettimana(GiornoSettimana giornosettimana) {
        this.giornosettimana = giornosettimana;
    }

    public Integer getAnticipoapertura() {
        return anticipoapertura;
    }

    public void setAnticipoapertura(Integer anticipoapertura) {
        this.anticipoapertura = anticipoapertura;
    }

    public Integer getOraapertura() {
        return oraapertura;
    }

    public void setOraapertura(Integer oraapertura) {
        this.oraapertura = oraapertura;
    }

    public Integer getMinutoapertura() {
        return minutoapertura;
    }

    public void setMinutoapertura(Integer minutoapertura) {
        this.minutoapertura = minutoapertura;
    }

    public Integer getOrachiusura() {
        return orachiusura;
    }

    public void setOrachiusura(Integer orachiusura) {
        this.orachiusura = orachiusura;
    }

    public Integer getMinutochiusura() {
        return minutochiusura;
    }

    public void setMinutochiusura(Integer minutochiusura) {
        this.minutochiusura = minutochiusura;
    }

    public Integer getAnticipochiusura() {
        return anticipochiusura;
    }

    public void setAnticipochiusura(Integer anticipochiusura) {
        this.anticipochiusura = anticipochiusura;
    }
}
