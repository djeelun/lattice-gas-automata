public class Region {
    private boolean[][] region;
    private int width, height;

    public Region(int width, int height){
        this.width = width;
        this.height = height;

        this.region = new boolean[height][width];
    }

    //TODO: implementar
    public void addLine(int startX, int startY, int endX, int endY, boolean inverted){

    }


    public void addRectangle(int startX, int startY, int endX, int endY, boolean inverted){
        for(int i=startY; i<=endY; i++){
            for(int j=startX; j<=endX; j++){
                this.region[i][j] = !inverted;
            }
        }
    }

    public void addRectangleBorders(int startX, int startY, int endX, int endY, int borderSize, boolean inverted){
        for(int borderIndex=0; borderIndex < borderSize; borderIndex++){
            //left border
            addRectangle(borderIndex, startY,  borderIndex, endY, inverted);
            //right border
            addRectangle(endX-borderIndex, startY,  endX-borderIndex, endY, inverted);
            //top border
            addRectangle(startX, borderIndex,  endX, borderIndex, inverted);
            //bottom border
            addRectangle(startX, endY-borderIndex,  endX, endY-borderIndex, inverted);
        }
        
    }

    public void printRegion(){
        final String OFFSET = " ";
        for(int i=0; i<height; i++){
            StringBuilder sb = new StringBuilder(i%2 == 0 ? OFFSET : "");
            for(int j=0; j<width; j++){
                sb.append(OFFSET).append(region[i][j] ? '*' : '.');
            }
            System.out.println(sb);
        }
    }

    public boolean[][] getRegion() {
        return region;
    }
}
