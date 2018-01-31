/*
 * File: MathEvaluator.java
 *
 * Desc: Math evaluator.
 *
 * Notes:
 *     - Initial version of this file came from (The-Son LAI) at:
 *          http://lts.online.fr/dev/java/math.evaluator/
 *
 * Gilbarco Inc. 2008
 *
 * History:
 *    2008.09.11.thu.Scott Turner  v01.0.01
 *       - Created (from above URL).
 */

//----------------------------------------------------------------------
package troubleshoot.util;

//----------------------------------------------------------------------
import java.util.HashMap;

//----------------------------------------------------------------------
/************************************************************************
 * Mathematic expression evaluator. <p>
 *
 * <UL> Supported functions:
 * <LI> +
 * <LI> -
 * <LI> *
 * <LI> /
 * <LI> ^
 * <LI> %
 * <LI> sin
 * <LI> cos
 * <LI> tan
 * <LI> asin
 * <LI> acos
 * <LI> atan
 * <LI> sqrt
 * <LI> sqr
 * <LI> log
 * <LI> min
 * <LI> max
 * <LI> ceil
 * <LI> floor
 * <LI> abs
 * <LI> neg
 * <LI> rnd
 * </UL>
 *
 * <UL> Built in variables:
 * <LI> PI - Pi.
 * <LI> D2R - Converts degrees to radians.
 * <LI> R2D - Converts radians to degrees.
 * </UL>
 *
 * Initial version of this file came from (The-Son LAI) at:
 * http://lts.online.fr/dev/java/math.evaluator/
 ************************************************************************/
public class EdtMathEval
{
    // ******************************************************************
    // PUBLIC FIELDS.
    // ******************************************************************

    // ******************************************************************
    // PROTECTED FIELDS.
    // ******************************************************************

    protected static Operator[] _operators  = null;

    // ******************************************************************
    // PRIVATE FIELDS.
    // ******************************************************************

    // Nodes.
    private Node _node = null;

    // Expression.
    private String _expression = null;

    // Variables.
    private HashMap<String,Double> _variables = null;

    // Eval exception.
    private Exception _evalException = null;

    // ******************************************************************
    // CONSTRUCTOR.
    // ******************************************************************

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create. <p>
     * setExpression() must be called to set expression to evaluate.
     */
    public EdtMathEval()
    {
        init();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Create. <p>
     *
     * @param s Expression to evaluate.
     */
    public EdtMathEval(String s)
    {
        init();
        setExpression(s);
    }

    // ******************************************************************
    // FINALIZER.
    // ******************************************************************

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    public void finalize() throws Throwable
    {
    }

    // ******************************************************************
    // PUBLIC METHODS       (general, getter, setter, interface imp)
    // ******************************************************************

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert to string. <p>
     *
     * @return The string.
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder(100);

        sb.append(this.getClass().getSimpleName());
        sb.append(":");

        sb.append(EdtStringUtil.toString("\nexpression=",getExpression()));


        return sb.toString();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Reset. <p>
     */
    public void reset()
    {
        _node       = null;
        _expression = null;

        initVariables();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get expression to evaluate. <p>
     *
     * @return The value or null.
     */
    public String getExpression()
    {
        return _expression;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Set expression to evaluation. <p>
     *
     * @param s The expression.
     */
    public void setExpression(String s)
    {
        _expression = s;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Add variable. <p>
     *
     * @param v Variable name.
     * @param val Variable value.
     */
    public void addVariable(String v, double val)
    {
        addVariable(v, new Double(val));
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Add variable. <p>
     *
     * @param v Variable name.
     * @param val Variable value.
     */
    public void addVariable(String v, Double val)
    {
        _variables.put(v, val);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get variable value. <p>
     *
     * @param v Variable name.
     * @return Variable value.
     */
    public Double getVariable(String v)
    {
        return (Double) _variables.get(v);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Evaluate expression and return result. <p>
     *
     * @return Result or null if evaluation problem.
     */
    public Double getValue()
    {
        if (_expression == null) return null;

        try
        {
            _node = new Node(_expression);
            return evalNode(_node);
        }
        catch (Exception e)
        {
            _evalException = e;
            return null;
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get evaluation exception. <p>
     *
     * @return The value or null.
     */
    public Exception getEvalException()
    {
        return _evalException;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Trace the binary tree for debug. <p>
     */
    public void trace()
    {
        try
        {
            _node = new Node(_expression);
            _node.trace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // ******************************************************************
    // PROTECTED METHODS.
    // ******************************************************************

    // ******************************************************************
    // PRIVATE METHODS.
    // ******************************************************************

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Init. <p>
     */
    private void init()
    {
        if ( _operators == null ) initializeOperators();

        initVariables();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Init default variables. <p>
     */
    private void initVariables()
    {
        _variables = new HashMap<String,Double>();

        addVariable("PI",Math.PI);
        addVariable("D2R",Math.PI / 180);
        addVariable("R2D",180 / Math.PI);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Evaluate node. <p>
     *
     * @param n Node.
     * @return Result.
     */
    private static Double evalNode(Node n)
    {
        if ( n.hasOperator() && n.hasChild() )
        {
            if ( n.getOperator().getType() == 1 )
                n.setValue ( evalExpression( n.getOperator(), evalNode( n.getLeft() ), null ) );
            else if ( n.getOperator().getType() == 2 )
                n.setValue( evalExpression( n.getOperator(), evalNode( n.getLeft() ), evalNode( n.getRight() ) ) );
        }
        return n.getValue();
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Evaluate expression. <p>
     *
     * @param o Operator.
     * @param f1 Operator param 1.
     * @param f2 Operator param 2.
     * @return Result.
     */
    private static Double evalExpression(Operator o, Double f1, Double f2)
    {
        String op       = o.getOperator();
        Double res      = null;

        if       ( "+".equals(op) )     res = new Double( f1.doubleValue() + f2.doubleValue() );
        else if  ( "-".equals(op) )     res = new Double( f1.doubleValue() - f2.doubleValue() );
        else if  ( "*".equals(op) )     res = new Double( f1.doubleValue() * f2.doubleValue() );
        else if  ( "/".equals(op) )     res = new Double( f1.doubleValue() / f2.doubleValue() );
        else if  ( "^".equals(op) )     res = new Double( Math.pow(f1.doubleValue(), f2.doubleValue()) );
        else if  ( "%".equals(op) )     res = new Double( f1.doubleValue() % f2.doubleValue() );
        else if  ( "&".equals(op) )     res = new Double( f1.doubleValue() + f2.doubleValue() ); // todo
        else if  ( "|".equals(op) )     res = new Double( f1.doubleValue() + f2.doubleValue() ); // todo
        else if  ( "cos".equals(op) )   res = new Double( Math.cos(f1.doubleValue()) );
        else if  ( "sin".equals(op) )   res = new Double( Math.sin(f1.doubleValue()) );
        else if  ( "tan".equals(op) )   res = new Double( Math.tan(f1.doubleValue()) );
        else if  ( "acos".equals(op) )  res = new Double( Math.acos(f1.doubleValue()) );
        else if  ( "asin".equals(op) )  res = new Double( Math.asin(f1.doubleValue()) );
        else if  ( "atan".equals(op) )  res = new Double( Math.atan(f1.doubleValue()) );
        else if  ( "sqr".equals(op) )   res = new Double( f1.doubleValue() * f1.doubleValue() );
        else if  ( "sqrt".equals(op) )  res = new Double( Math.sqrt(f1.doubleValue()) );
        else if  ( "log".equals(op) )   res = new Double( Math.log(f1.doubleValue()) );
        else if  ( "min".equals(op) )   res = new Double( Math.min(f1.doubleValue(), f2.doubleValue()) );
        else if  ( "max".equals(op) )   res = new Double( Math.max(f1.doubleValue(), f2.doubleValue()) );
        else if  ( "exp".equals(op) )   res = new Double( Math.exp(f1.doubleValue()) );
        else if  ( "floor".equals(op) ) res = new Double( Math.floor(f1.doubleValue()) );
        else if  ( "ceil".equals(op) )  res = new Double( Math.ceil(f1.doubleValue()) );
        else if  ( "abs".equals(op) )   res = new Double( Math.abs(f1.doubleValue()) );
        else if  ( "neg".equals(op) )   res = new Double( - f1.doubleValue() );
        else if  ( "rnd".equals(op) )   res = new Double( Math.random() * f1.doubleValue() );

        return res;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Init operators. <p>
     */
    private void initializeOperators()
    {
        _operators     = new Operator[25];
        _operators[0]  = new Operator("+"        , 2, 0);
        _operators[1]  = new Operator("-"        , 2, 0);
        _operators[2]  = new Operator("*"        , 2, 10);
        _operators[3]  = new Operator("/"        , 2, 10);
        _operators[4]  = new Operator("^"        , 2, 10);
        _operators[5]  = new Operator("%"        , 2, 10);
        _operators[6]  = new Operator("&"        , 2, 0);
        _operators[7]  = new Operator("|"        , 2, 0);
        _operators[8]  = new Operator("cos"      , 1, 20);
        _operators[9]  = new Operator("sin"      , 1, 20);
        _operators[10] = new Operator("tan"      , 1, 20);
        _operators[11] = new Operator("acos"     , 1, 20);
        _operators[12] = new Operator("asin"     , 1, 20);
        _operators[13] = new Operator("atan"     , 1, 20);
        _operators[14] = new Operator("sqrt"     , 1, 20);
        _operators[15] = new Operator("sqr"      , 1, 20);
        _operators[16] = new Operator("log"      , 1, 20);
        _operators[17] = new Operator("min"      , 2, 0);
        _operators[18] = new Operator("max"      , 2, 0);
        _operators[19] = new Operator("exp"      , 1, 20);
        _operators[20] = new Operator("floor"    , 1, 20);
        _operators[21] = new Operator("ceil"     , 1, 20);
        _operators[22] = new Operator("abs"      , 1, 20);
        _operators[23] = new Operator("neg"      , 1, 20);
        _operators[24] = new Operator("rnd"      , 1, 20);
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Get operators. <p>
     *
     * @return Operators.
     */
    private Operator[] getOperators()
    {
        return _operators;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Convert string to double value. <p>
     *
     * @param s String.
     * @return The value.
     */
    private Double getDouble(String s)
    {
        if ( s == null ) return null;

        Double res = null;
        try
        {
            res = new Double(Double.parseDouble(s));
        }
        catch(Exception e)
        {
            return getVariable(s);
        }

        return res;
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Print trace string to stderr. <p>
     *
     * @param s String to print.
     */
    @SuppressWarnings("unused")
    private static void _D(String s)
    {
        System.err.println(s);
    }

    // ******************************************************************
    // INNER CLASSES.
    // ******************************************************************

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Operator. <p>
     */
    private class Operator
    {
        private String op;
        private int type;
        private int priority;

        public Operator(String o, int t, int p)
        {
            op = o;
            type = t;
            priority = p;
        }

        public String getOperator()
        {
            return op;
        }

        public int getType()
        {
            return type;
        }

        public int getPriority()
        {
            return priority;
        }
    }

    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    /**
     * Node. <p>
     */
    private class Node
    {
        public String   nString         = null;
        public Operator nOperator       = null;
        public Node     nLeft           = null;
        public Node     nRight          = null;
        public int      nLevel          = 0;
        public Double   nValue          = null;

        public Node(String s) throws Exception
        {
            init(null, s, 0);
        }

        public Node(Node parent, String s, int level) throws Exception
        {
            init(parent, s, level);
        }

        private void init(Node parent, String s, int level) throws Exception
        {
            s = removeIllegalCharacters(s);
            s = removeBrackets(s);
            s = addZero(s);
            if ( checkBrackets(s) != 0 ) throw new Exception("Wrong number of brackets in [" + s + "]");

            nString                     = s;
            nValue                              = getDouble(s);
            nLevel                              = level;
            int sLength                 = s.length();
            int inBrackets              = 0;
            int startOperator   = 0;

            for (int i=0; i<sLength; i++)
            {
                if ( s.charAt(i) == '(' )
                    inBrackets++;
                else if ( s.charAt(i) == ')' )
                    inBrackets--;
                else
                {
                    // the expression must be at "root" level
                    if ( inBrackets == 0 )
                    {
                        Operator o = getOperator(nString,i);
                        if ( o != null )
                        {
                            // if first operator or lower priority operator
                            if ( nOperator == null || nOperator.getPriority() >= o.getPriority() )
                            {
                                nOperator               = o;
                                startOperator   = i;
                            }
                        }
                    }
                }
            }

            if ( nOperator != null )
            {
                // one operand, should always be at the beginning
                if ( startOperator==0 && nOperator.getType() == 1 )
                {
                    // the brackets must be ok
                    if ( checkBrackets( s.substring( nOperator.getOperator().length() ) ) == 0 )
                    {
                        nLeft  = new Node( this, s.substring( nOperator.getOperator().length() ) , nLevel + 1);
                        nRight = null;
                        return;
                    }
                    else
                        throw new Exception("Error during parsing... missing brackets in [" + s + "]");
                }
                // two operands
                else if ( startOperator > 0 && nOperator.getType() == 2 )
                {
                    // swt - nOperator = nOperator;
                    nLeft       = new Node( this, s.substring(0,  startOperator), nLevel + 1 );
                    nRight      = new Node( this, s.substring(startOperator + nOperator.getOperator().length()), nLevel + 1);
                }
            }
        }

        private Operator getOperator(String s, int start)
        {
            Operator[] operators = getOperators();
            String temp = s.substring(start);
            temp = getNextWord(temp);
            for (int i=0; i<operators.length; i++)
            {
                if ( temp.startsWith(operators[i].getOperator()) )
                    return operators[i];
            }
            return null;
        }

        private String getNextWord(String s)
        {
            int sLength = s.length();
            for (int i=1; i<sLength; i++)
            {
                char c = s.charAt(i);
                if ( (c > 'z' || c < 'a') && (c > '9' || c < '0') )
                    return s.substring(0, i);
            }
            return s;
        }

        /**
         * checks if there is any missing brackets
         * @return true if s is valid
         */
        protected int checkBrackets(String s)
        {
            int sLength         = s.length();
            int inBracket   = 0;

            for (int i=0; i<sLength; i++)
            {
                if      ( s.charAt(i) == '(' && inBracket >= 0 )
                    inBracket++;
                else if ( s.charAt(i) == ')' )
                    inBracket--;
            }

            return inBracket;
        }

        /**
         * returns a string that doesnt start with a + or a -
         */
        protected String addZero(String s)
        {
            if ( s.startsWith("+") || s.startsWith("-") )
            {
                int sLength     = s.length();
                for (int i=0; i<sLength; i++)
                {
                    if ( getOperator(s, i) != null )
                        return "0" + s;
                }
            }

            return s;
        }

        /**
         * displays the tree of the expression
         */
        public void trace()
        {
            String op = getOperator() == null ? " " : getOperator().getOperator() ;
            _D( op + " : " + getString() );
            if ( this.hasChild() )
            {
                if ( hasLeft() )
                    getLeft().trace();
                if ( hasRight() )
                    getRight().trace();
            }
        }

        protected boolean hasChild()
        {
            return ( nLeft != null || nRight != null );
        }

        protected boolean hasOperator()
        {
            return ( nOperator != null );
        }

        protected boolean hasLeft()
        {
            return ( nLeft != null );
        }

        protected Node getLeft()
        {
            return nLeft;
        }

        protected boolean hasRight()
        {
            return ( nRight != null );
        }

        protected Node getRight()
        {
            return nRight;
        }

        protected Operator getOperator()
        {
            return nOperator;
        }

        protected Double getValue()
        {
            return nValue;
        }

        protected void setValue(Double f)
        {
            nValue = f;
        }

        protected String getString()
        {
            return nString;
        }

        /**
         * Removes spaces, tabs and brackets at the begining
         */
        public String removeBrackets(String s)
        {
            String res = s;
            if ( s.length() > 2 && res.startsWith("(") && res.endsWith(")") && checkBrackets(s.substring(1,s.length()-1)) == 0 )
            {
                res = res.substring(1, res.length()-1 );
            }
            if ( res != s )
                return removeBrackets(res);
            else
                return res;
        }

        /**
         * Removes illegal characters
         */
        public String removeIllegalCharacters(String s)
        {
            char[] illegalCharacters = { ' ' };
            String res = s;

            for ( int j=0; j<illegalCharacters.length; j++)
            {
                int i = res.lastIndexOf(illegalCharacters[j], res.length());
                while ( i != -1 )
                {
                    String temp = res;
                    res = temp.substring(0,i);
                    res += temp.substring(i + 1);
                    i = res.lastIndexOf(illegalCharacters[j], s.length());
                }
            }
            return res;
        }

        protected void _D(String s)
        {
            String nbSpaces = "";
            for (int i=0; i<nLevel; i++) nbSpaces += "  ";
            System.out.println(nbSpaces + "|" + s);
        }
    }

    // ******************************************************************
    // NATIVE METHODS.
    // ******************************************************************

    // ******************************************************************
    // MAIN.
    // ******************************************************************
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}

//----------------------------------------------------------------------
