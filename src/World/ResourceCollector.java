package World;

import Game.GraphicsPanel;

public class ResourceCollector extends Building {
    private int tier, collectAmount;

    public ResourceCollector(GraphicsPanel graphicsPanel, Tile tile, int type){
        super(graphicsPanel, tile, type);
        tier = 1;
        collectAmount = 100*tier;
    }

    public void collectResources(){
        if(super.getType() == super.FARM){
            super.getGraphicsPanel().getPlayer().setFood(super.getGraphicsPanel().getPlayer().getFood() + collectAmount);
        } else if(super.getType() == super.WOODMILL){
            super.getGraphicsPanel().getPlayer().setWood(super.getGraphicsPanel().getPlayer().getWood() + collectAmount);
        }
    }

    public void upgrade(){
        if(tier < 3) {
            tier++;
            collectAmount = 100 * tier;
        }
    }
}