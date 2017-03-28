package urt.stats.core.xmlimport;

public class JaxbError implements Comparable<JaxbError> {

    /**
     * Error line
     */
    private int line;
    
    /**
     * Error column
     */
    private int col;
    
    /**
     * Error description
     */
    private String description;

    /**
     * Parameterized constructor
     * 
     * @param line int error line
     * @param col int error col
     * @param description {@link String} erro description
     */
    public JaxbError(int line, int col, String description) {
        this.line = line;
        this.col = col;
        this.description = description;
    }

    /**
     * @return int error line
     */
    public int getLine() {
        return line;
    }

    /**
     * @param line int error line
     */
    public void setLine(int line) {
        this.line = line;
    }

    /**
     * @return error col
     */
    public int getCol() {
        return col;
    }

    /**
     * @param col error col
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * @return {@link String} error description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description {@link String} error description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + col;
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + line;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JaxbError other = (JaxbError) obj;
        if (col != other.col)
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (line != other.line)
            return false;
        return true;
    }

    public String toString() {
        return "JaxbError [line=" + line + ", col=" + col + ", description="
                + description + "]";
    }

    public int compareTo(JaxbError o) {
        return this.line > o.line ? +1 : this.line < o.line ? -1 : 
               this.col > o.col ? +1 : this.col < o.col ? -1 :
               0;
    }
}