package com.wayne.tictoe;

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

import com.example.tictoe.R;

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
    int whoFirst = 0;
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

        TextView whiteCat = findViewById(R.id.textViewP1);
        TextView blackCat = findViewById(R.id.textViewP2);
        TextView round = findViewById(R.id.textViewP3);
        TextView whoGoFirst = findViewById(R.id.textViewP4);

        whiteCat.setText(getString(R.string.prefix_white_cat) + whiteWin);
        blackCat.setText(getString(R.string.prefix_black_cat) + blackWin);
        round.setText(getString(R.string.round_string, countRound));


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
                for (int i = 0; i < imageViews.size(); i++) {
                    space.set(i, NOTHING);
                    imageViews.get(i).setImageDrawable(null);
                }
                gameSet = false;
                countRound++;
                //誰先手
                round.setText(getString(R.string.round_string, countRound));
                if (whoFirst % 2 == 0) {
                    oTurn = false;
                    whoGoFirst.setText(R.string.black_cat_go_first);
                } else if (whoFirst % 2 == 1) {
                    oTurn = true;
                    whoGoFirst.setText(R.string.white_cat_go_first);
                }
                whoFirst++;
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
            Toast.makeText(this, getString(R.string.please_click_the_right_area), Toast.LENGTH_SHORT).show();
        }


    }

    private void checkWinner() {
        //oTurn tells who is the winner
        TextView whiteCat = findViewById(R.id.textViewP1);
        TextView blackCat = findViewById(R.id.textViewP2);


        for (int[] indices : winningIndices) {
            if (space.get(indices[0]) != NOTHING && space.get(indices[0]).equals(space.get(indices[1])) && space.get(indices[1]).equals(space.get(indices[2]))) {
                if (oTurn) {
                    Toast.makeText(this, getString(R.string.white_cat_winning), Toast.LENGTH_SHORT).show();
                    whiteWin++;
                    whiteCat.setText(getString(R.string.prefix_white_cat) + whiteWin);
                    gameSet = true;
                    break;
                } else {
                    Toast.makeText(this, getString(R.string.black_cat_winning), Toast.LENGTH_SHORT).show();
                    blackWin++;
                    blackCat.setText(getString(R.string.prefix_black_cat) + blackWin);
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
                if (wrapper.value == RIGHT) {
                    imageView.animate().rotation(-45).setDuration(500);
                    wrapper.value = LEFT;
                } else if (wrapper.value == LEFT)
                    imageView.animate().rotation(0).setDuration(500);

            }
        });
    }

}

class Wrapper<T> {
    T value;
}


