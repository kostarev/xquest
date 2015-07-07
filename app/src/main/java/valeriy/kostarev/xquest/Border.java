package valeriy.kostarev.xquest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by valerik on 07.12.2014.
 */
public class Border {

    private final int topX;
    private final int topY;
    private final int holeX;
    private final int textWidth;
    private final int textHeight;
    private Game game;
    private Paint paint;
    private int width, height, weight, textX, textY, holeWidth;
    private boolean isOpened;
    public Rect top1, top2, left, bottom, right, hole;
    private String holeText;
    private boolean opened;
    public ExitHit exitHit;
    private Bitmap wallBitmap, cornerLTBitmap, cornerRTBitmap, cornerLDBitmap, cornerRDBitmap, topBitmap, botBitmap, leftBitmap, rightBitmap;

    public Border(Game game) {
        this.game = game;
        paint = new Paint();

        //Толщина рамки
        weight = (int) (game.getGameScreenWidth() / 60);
        //Ширина прохода на следующий уровень
        holeWidth = weight * 10;

        //Ширина
        width = game.getGameScreenWidth() + 2 * weight;
        //Высота
        height = game.getGameScreenHeight() + 2 * weight;

        top1 = new Rect(0, 0, width / 2 - holeWidth / 2, weight);
        top2 = new Rect(width / 2 + holeWidth / 2, 0, width, weight);
        left = new Rect(0, weight, weight, height - weight);
        right = new Rect(width - weight, weight, width, height - weight);
        bottom = new Rect(0, height - weight, width, height);
        hole = new Rect(width / 2 - holeWidth / 2, 0, width / 2 + holeWidth / 2, weight);

        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(weight / 2);
        holeText = game.gameView.activity.getBaseContext().getString(R.string.NextWave);

        topX = width / 2 - holeWidth / 2;
        topY = height - weight;
        holeX = width / 2 + holeWidth / 2;
        textWidth = width / 2 - (int) paint.measureText(holeText) / 2;
        textHeight = weight / 2 + (int) paint.getTextSize() / 2;

        game.winRect = new Rect(width / 2 - holeWidth, -100, width / 2 + holeWidth, 0);

        //Подсказка для выхода
        exitHit = new ExitHit(game);

        cornerLTBitmap = Bitmap.createBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(game.gameView.activity.getResources(), R.drawable.corner), weight * 5, weight * 5, true), 0, 0, weight, weight);
        cornerRTBitmap = Bitmap.createBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(game.gameView.activity.getResources(), R.drawable.corner), weight * 5, weight * 5, true), weight * 4 - 1, 0, weight, weight);
        cornerLDBitmap = Bitmap.createBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(game.gameView.activity.getResources(), R.drawable.corner), weight * 5, weight * 5, true), 0, weight * 4 - 1, weight, weight);
        cornerRDBitmap = Bitmap.createBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(game.gameView.activity.getResources(), R.drawable.corner), weight * 5, weight * 5, true), weight * 4 - 1, weight * 4 - 1, weight, weight);
        topBitmap      = Bitmap.createBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(game.gameView.activity.getResources(), R.drawable.corner), weight * 5, weight * 5, true), weight, 0, weight * 3, weight);
        botBitmap      = Bitmap.createBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(game.gameView.activity.getResources(), R.drawable.corner), weight * 5, weight * 5, true), weight, 4 * weight, weight * 3, weight);
        leftBitmap     = Bitmap.createBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(game.gameView.activity.getResources(), R.drawable.corner), weight * 5, weight * 5, true), 0, weight, weight, weight * 3);
        rightBitmap    = Bitmap.createBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(game.gameView.activity.getResources(), R.drawable.corner), weight * 5, weight * 5, true), 4 * weight, weight, weight, weight * 3);
    }

    public void draw(Canvas canvas) {
        int gameX = game.getGameScreenX0() - weight;
        int gameY = game.getGameScreenY0() - weight;

        //paint.setColor(Color.rgb(180, 100, 40));
        //paint.setStyle(Paint.Style.FILL);
        top1.set(gameX, gameY, gameX + topX, gameY + weight);
        //canvas.drawRect(top1, paint);
        for (int x = gameX; x < gameX + topX - weight; x = x + 3*weight) {
            canvas.drawBitmap(topBitmap, x, gameY, paint);
        }

        //canvas.drawRect(top2, paint);
        top2.set(gameX + holeX, gameY, gameX + width, gameY + weight);
        for (int x = gameX + holeX; x < gameX + width - weight; x = x + 3*weight) {
            canvas.drawBitmap(topBitmap, x, gameY, paint);
        }


        left.set(gameX, gameY + weight, gameX + weight, gameY + topY);
        //canvas.drawRect(left, paint);
        for (int y = gameY + weight; y < gameY + height - weight; y = y + 3*weight) {
            canvas.drawBitmap(leftBitmap, gameX, y, paint);
        }

        right.set(gameX + width - weight, gameY + weight, gameX + width, gameY + topY);
        //canvas.drawRect(right, paint);
        for (int y = gameY + weight; y < gameY + topY - weight; y = y + 3*weight) {
            canvas.drawBitmap(rightBitmap, gameX + width - weight, y, paint);
        }


        bottom.set(gameX, gameY + topY, gameX + width, gameY + height);
        //canvas.drawRect(bottom, paint);
        for (int x = gameX; x < gameX + width - weight; x = x + 3*weight) {
            canvas.drawBitmap(botBitmap, x, gameY + height - weight, paint);
        }

        canvas.drawBitmap(cornerLTBitmap, gameX, gameY, paint);
        canvas.drawBitmap(cornerRTBitmap, gameX + width - weight, gameY, paint);
        canvas.drawBitmap(cornerLDBitmap, gameX, gameY + height - weight, paint);
        canvas.drawBitmap(cornerRDBitmap, gameX + width - weight, gameY + height - weight, paint);


        game.winRect.set(gameX + width / 2 - holeWidth, gameY - 100, gameX + width / 2 + holeWidth, gameY);

        if (!isOpened) {
            textX = gameX + textWidth;
            textY = gameY + textHeight;
            paint.setColor(Color.rgb(130, 100, 230));
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            hole.set(gameX + topX, gameY, gameX + holeX, gameY + weight);
            canvas.drawRect(hole, paint);
            paint.setColor(Color.BLACK);
            canvas.drawText(holeText, textX, textY, paint);
        } else {
            //Стрелка-подсказка на выход
            exitHit.draw(canvas);
        }

    }


    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public boolean isIntersect(Rect rect) {
        if (top1.intersect(rect) || top2.intersect(rect) || left.intersect(rect) || right.intersect(rect) || bottom.intersect(rect) || (!isOpened && hole.intersect(rect))) {
            return true;
        }
        return false;
    }
}
