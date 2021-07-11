package com.example.tictoe;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int O_SYMBOL = 0;
    static final int X_SYMBOL = 1;
    static final int NOTHING = 2;

    static final int RIGHT = 0;
    static final int LEFT = 1;
    static final int CENTER = 2;
    int whiteWin = 0;
    int blackWin = 0;
    boolean gameSet = false;
    boolean oTurn = true;
    int countRound = 1;
    List<Integer> space = new ArrayList<>();

    static final int[][] winningIndices =
            new int[][]{
                    {0, 1, 2},
                    {3, 4, 5},
                    {6, 7, 8},
                    {0, 3, 6},
                    {1, 4, 7},
                    {2, 5, 8},
                    {0, 4, 8},
                    {2, 4, 6}
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //各個格子的狀態 null=沒有,0=黑貓,1=另一隻貓
        for (int i = 0; i < 9; i++) {
            space.add(NOTHING);
        }


        //抓ID
        List<ImageView> imageViews = new ArrayList<>();
        imageViews.add(findViewById(R.id.button1));
        imageViews.add(findViewById(R.id.button2));
        imageViews.add(findViewById(R.id.button3));
        imageViews.add(findViewById(R.id.button4));
        imageViews.add(findViewById(R.id.button5));
        imageViews.add(findViewById(R.id.button6));
        imageViews.add(findViewById(R.id.button7));
        imageViews.add(findViewById(R.id.button8));
        imageViews.add(findViewById(R.id.button9));
        Button resetButton = findViewById(R.id.buttonReset);


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView round = findViewById(R.id.textViewP3);
                for (int i = 0; i < imageViews.size(); i++) {
                    space.set(i, NOTHING);
                    imageViews.get(i).setImageDrawable(null);
                }
                gameSet = false;
                countRound++;
                round.setText("第 " + countRound + " 局");
            }
        });

        for (int i = 0; i < imageViews.size(); i++) {
            final int index = i;
            ImageView imageView = imageViews.get(i);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!gameSet)
                        onImageClick(imageView, index);
                }
            });
        }
    }

    private void onImageClick(ImageView imageView, int index) {
        //oTurn -> 誰在下
        //index -> 下在哪
        //imageView -> 哪一個imageView 要被 setImageResource
        if (oTurn && space.get(index) == NOTHING) {
            space.set(index, O_SYMBOL);
            imageView.setImageResource(R.drawable.tictoe2);
            animation(imageView);
            checkWinner();
            oTurn = !oTurn;
        } else if (!oTurn && space.get(index) == NOTHING) {
            space.set(index, X_SYMBOL);
            imageView.setImageResource(R.drawable.tictoe3);
            animation(imageView);
            checkWinner();
            oTurn = !oTurn;
        } else {
            Toast.makeText(this, "請點擊別處！", Toast.LENGTH_SHORT).show();
        }


    }

    private void checkWinner() {
        //oTurn tells who is the winner
        TextView whiteCat = findViewById(R.id.textViewP1);
        TextView blackCat = findViewById(R.id.textViewP2);


        for (int[] indices : winningIndices) {
            if (space.get(indices[0]) != NOTHING && space.get(indices[0]).equals(space.get(indices[1])) && space.get(indices[1]).equals(space.get(indices[2]))) {
                if (oTurn) {
                    Toast.makeText(this, "白貓贏了", Toast.LENGTH_SHORT).show();
                    whiteWin++;
                    whiteCat.setText("白貓：" + whiteWin);
                    gameSet = true;
                    break;
                } else {
                    Toast.makeText(this, "黑貓贏了", Toast.LENGTH_SHORT).show();
                    blackWin++;
                    blackCat.setText("黑貓：" + blackWin);
                    gameSet = true;
                    break;
                }
            }
        }
    }

    private void animation(ImageView imageView) {
        Wrapper<Integer> wrapper = new Wrapper<>();
        wrapper.value = RIGHT;

        imageView.setRotation(0);
        imageView.setAlpha(0f);
        ViewPropertyAnimator animator = imageView.animate().alpha(1).setDuration(2000).rotation(45).setDuration(500);
        animator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(wrapper.value == RIGHT){
                    imageView.animate().rotation(-45).setDuration(500);
                    wrapper.value = LEFT;
                } else if(wrapper.value == LEFT)
                    imageView.animate().rotation(0).setDuration(500);

            }
        });
    }

}

class Wrapper<T>{
    T value;
}


