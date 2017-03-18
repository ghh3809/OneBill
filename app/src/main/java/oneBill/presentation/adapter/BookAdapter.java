package oneBill.presentation.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;
import oneBill.domain.entity.Book;
import oneBill.domain.entity.Log;
import oneBill.presentation.AddRecordActivity;
import oneBill.presentation.activity.AtyAddRecord;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 豪豪 on 2017/3/18 0018.
 */

/**
 * 将账本展示到ListView上
 */
public class BookAdapter extends ArrayAdapter<Book> {

    private int resourceId;
    private Context context;
    private Book book;
    private Actioner actioner;
    //随机颜色，利用book的HashCode计算
    int[] randomColor = new int[]{R.color.darkGreen, R.color.lightGreen, R.color.darkBrown, R.color.lightBrown};
    int progress;

    public BookAdapter(Actioner actioner, Context context, int textViewResourceId, List<Book> bookList) {
        super(context, textViewResourceId, bookList);
        this.resourceId = textViewResourceId;
        this.context = context;
        this.actioner = actioner;
        SharedPreferences pref = context.getSharedPreferences("setting", MODE_PRIVATE);
        progress = pref.getInt("MaxDetail", 1);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        book = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }
        TextView textviewBand = (TextView) view.findViewById(R.id.textviewBand);
        TextView textviewBookName = (TextView) view.findViewById(R.id.textviewBookName);
        ImageView imageviewAddRecord = (ImageView) view.findViewById(R.id.imageviewAddRecord);
        TextView textviewSum = (TextView) view.findViewById(R.id.textviewSum);
        TextView textviewRecent = (TextView) view.findViewById(R.id.textviewRecent);
        textviewBand.setBackgroundResource(randomColor[book.getName().hashCode() % 4]);
        textviewBookName.setText(book.getName());
        //首个显示详细内容，后续显示简要内容
        if (position < progress) {
            textviewSum.setVisibility(View.VISIBLE);
            textviewRecent.setVisibility(View.VISIBLE);
            textviewSum.setText("¥ " + actioner.GetSum(book.getName()));
            try {
                Log recentRecord = actioner.GetLatestRecord(book.getName());
                textviewRecent.setText("最近消费：" + recentRecord.getTime() + " ¥ " + recentRecord.getAmount() + " " + recentRecord.getType());
            } catch (Exception e){
                textviewRecent.setText("无最近消费");
            }
        } else {
            textviewSum.setVisibility(View.GONE);
            textviewRecent.setVisibility(View.GONE);
        }
        imageviewAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODOe
                        Intent intent = new Intent(BookAdapter.this.context, AtyAddRecord.class);
//                Intent intent = new Intent(BookAdapter.this.context, AddRecordActivity.class);
                intent.putExtra("bookName", book.getName());
                BookAdapter.this.context.startActivity(intent);
            }
        });
        return view;
    }

}
