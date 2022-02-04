package com.giracle.calculatrice;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity  extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_calcul;

    // implémentation bouton
    Button bouton1;
    Button bouton2;
    Button bouton3;
    Button bouton4;
    Button bouton5;
    Button bouton6;
    Button bouton7;
    Button bouton8;
    Button bouton9;
    Button bouton0;
    Button bouton_clear;
    Button bouton_fois;
    Button bouton_diviser;
    Button bouton_plus;
    Button bouton_moins;
    Button bouton_egal;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_calcul = findViewById(R.id.affichage);

        // récupération ID des boutons
        bouton1 = findViewById(R.id.bt_1);
        bouton2 = findViewById(R.id.bt_2);
        bouton3 = findViewById(R.id.bt_3);
        bouton4 = findViewById(R.id.bt_4);
        bouton5 = findViewById(R.id.bt_5);
        bouton6 = findViewById(R.id.bt_6);
        bouton7 = findViewById(R.id.bt_7);
        bouton8 = findViewById(R.id.bt_8);
        bouton9 = findViewById(R.id.bt_9);
        bouton0 = findViewById(R.id.bt_0);
        bouton_clear = findViewById(R.id.bt_clear);
        bouton_fois = findViewById(R.id.bt_fois);
        bouton_diviser = findViewById(R.id.bt_diviser);
        bouton_plus = findViewById(R.id.bt_plus);
        bouton_egal = findViewById(R.id.bt_egal);
        bouton_moins = findViewById(R.id.bt_moins);

        // écoute des boutons
        bouton0.setOnClickListener(this);
        bouton1.setOnClickListener(this);
        bouton2.setOnClickListener(this);
        bouton3.setOnClickListener(this);
        bouton4.setOnClickListener(this);
        bouton5.setOnClickListener(this);
        bouton6.setOnClickListener(this);
        bouton7.setOnClickListener(this);
        bouton8.setOnClickListener(this);
        bouton9.setOnClickListener(this);
        bouton_clear.setOnClickListener(this);
        bouton_plus.setOnClickListener(this);
        bouton_moins.setOnClickListener(this);
        bouton_fois.setOnClickListener(this);
        bouton_egal.setOnClickListener(this);
        bouton_diviser.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        // affectation de la valeurs de chaque bouton à la textView
        switch (view.getId()){
            case R.id.bt_0:
                setText(getValueBouton(bouton0));
                break;
            case R.id.bt_1:
                setText(getValueBouton(bouton1));
                break;
            case R.id.bt_2:
                setText(getValueBouton(bouton2));
                break;
            case R.id.bt_3:
                setText(getValueBouton(bouton3));
                break;
            case R.id.bt_4:
                setText(getValueBouton(bouton4));
                break;
            case R.id.bt_5:
                setText(getValueBouton(bouton5));
                break;
            case R.id.bt_6:
                setText(getValueBouton(bouton6));
                break;
            case R.id.bt_7:
                setText(getValueBouton(bouton7));
                break;
            case R.id.bt_8:
                setText(getValueBouton(bouton8));
                break;
            case R.id.bt_9:
                setText(getValueBouton(bouton9));
                break;
            case R.id.bt_clear:
                tv_calcul.setText("");
                break;
            case R.id.bt_plus:
                setText(getValueBouton(bouton_plus));
                break;
            case R.id.bt_moins:
                setText(getValueBouton(bouton_moins));
                break;
            case R.id.bt_fois:
                setText(getValueBouton(bouton_fois));
                break;
            case R.id.bt_diviser:
                setText(getValueBouton(bouton_diviser));
                break;
            case R.id.bt_egal:
                String formule = tv_calcul.getText().toString();
                tv_calcul.setText("");
                String resultatEval = String.valueOf(eval(formule));
                setText(resultatEval);
        }
    }


    /**
     * Affecte la valeur du bouton (récupéré avec getValueBouton) à la textView
     * @param valBT
     */
    private void setText(String valBT) {
        tv_calcul.setText(tv_calcul.getText().toString() + valBT);
    }


    /**
     * Récupère la valeur du bouton presser
     * return la valeur du bouton presser
     */
    public String getValueBouton(Button button){
        return button.getText().toString();
    }


    /**
     * Interpréteur mathématique
     * Source : https://stackoverflow.com/questions/3422673/how-to-evaluate-a-math-expression-given-in-string-form
     * @param str
     * @return le resultat
     */
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}