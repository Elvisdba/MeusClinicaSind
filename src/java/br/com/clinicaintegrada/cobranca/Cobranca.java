package br.com.clinicaintegrada.cobranca;

import br.com.clinicaintegrada.financeiro.Boleto;
import br.com.clinicaintegrada.financeiro.Movimento;
import br.com.clinicaintegrada.utils.DataHoje;
import java.util.Date;

public abstract class Cobranca {

    protected Movimento movimento;
    protected Boleto boleto;
    public final static String bancoDoBrasil = "001";
    public final static String caixaFederal = "104";
    public final static String itau = "341";
    public final static String bradesco = "237";
    public final static String real = "356";
    public final static String santander = "033";
    public final static String hsbc = "0";
    public final static String sicoob = "756";
    public final static int SICOB = 1;
    public final static int SINDICAL = 2;
    public final static int SIGCB = 3;

    public Cobranca(Movimento movimento, Boleto boleto) {
        setMovimento(movimento);
        setBoleto(boleto);
    }

    public abstract String moduloDez(String composicao);

    public abstract String moduloOnze(String composicao);

    public abstract String codigoBarras();

    public abstract String representacao();

    public abstract String codigoBanco();

    public String fatorVencimento(Date vencimento) {
        if (vencimento != null) {
            Date dataModel = DataHoje.converte("07/10/1997");
            long dias = vencimento.getTime() - dataModel.getTime();
            long total = dias / 86400000;
            return Long.toString(total);
        } else {
            return "";
        }
    }

    public String moduloOnzeDV(String composicao) {
        int i = composicao.length();
        int j = 2;
        int soma = 0;
        String m;
        while (i > 0) {
            if (j > 9) {
                j = 2;
            }
            m = composicao.substring(i - 1, i);
            soma += Integer.parseInt(m) * j;
            j++;
            i--;
        }

        if (soma < 11) {
            if ((soma == 1) || (soma == 0)) {
                return "1"; // a subtração abaixo pode resultar em 10 caso a soma seja "1". Apesar de ser um caso raro, estamos tratando esse posível erro.
            }
            return Integer.toString(11 - soma);
        }

        if (((11 - (soma % 11)) > 9)
                || ((11 - (soma % 11)) == 0) // Digito verificador geral nunca dara zero
                || ((11 - (soma % 11)) == 1)) {
            return "1";
        } else {
            return Integer.toString(11 - (soma % 11));
        }
    }

    public String getNossoNumeroFormatado() {
        return boleto.getBoletoComposto() + "-" + moduloOnze(boleto.getBoletoComposto());
    }

    public String getCedenteFormatado() {
        return boleto.getContaCobranca().getCodCedente().substring(0, 3) + "." + boleto.getContaCobranca().getCodCedente().substring(3) + "-"
                + moduloOnze(boleto.getContaCobranca().getContaBanco().getAgencia() + boleto.getContaCobranca().getCodCedente());
    }

    public String getAgenciaFormatada() {
        return boleto.getContaCobranca().getContaBanco().getAgencia();
    }

    public Boleto getBoleto() {
        return boleto;
    }

    public void setBoleto(Boleto boleto) {
        this.boleto = boleto;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }
}
