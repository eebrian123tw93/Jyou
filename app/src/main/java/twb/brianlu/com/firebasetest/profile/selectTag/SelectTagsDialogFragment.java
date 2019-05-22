package twb.brianlu.com.firebasetest.profile.selectTag;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.hhl.library.FlowTagLayout;

import java.util.ArrayList;
import java.util.List;

import twb.brianlu.com.firebasetest.R;
import twb.brianlu.com.firebasetest.core.BasePresenter;
import twb.brianlu.com.firebasetest.fbDataService.FirebaseDataService;

public class SelectTagsDialogFragment extends DialogFragment {
    private SelectTagsAdapter adapter;
    private FlowTagLayout tagLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // 设置主题的构造方法
        // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_select_tags, null);


        tagLayout = view.findViewById(R.id.tags_flow_layout);
        tagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);

        adapter = new SelectTagsAdapter(getContext());
//        loadTags();
        tagLayout.setAdapter(adapter);

        final List<String> tags = new ArrayList<>();
        tags.add("旅遊");
        tags.add("健行");
        tags.add("露營");
        tags.add("游泳");
        tags.add("衝浪");
        tags.add("浮潛");
        tags.add("潛水");
        tags.add("極限運動");
        tags.add("生存遊戲");
        tags.add("射擊");
        tags.add("红色");
        tags.add("黑色");
        tags.add("花边色");
        tags.add("深蓝色");
        tags.add("白色");
        tags.add("玫瑰红色");
        tags.add("紫黑紫兰色");
        tags.add("葡萄红色");
        tags.add("绿色");
        tags.add("彩虹色");
        tags.add("牡丹色");
        tags.add("周杰伦");
        tags.add("刘德华");
        tags.add("林俊杰");
        tags.add("邓紫棋");
        tags.add("华晨宇");
        tags.add("王力宏");
        tags.add("Linkin Park");
        tags.add("legend never die");
        tags.add("红莲之弓矢");
        tags.add("Let It Go");
        tags.add("青鸟");
        tags.add("in the end");
        tags.add("Hey Jude");
        tags.add("告白气球");
        tags.add("see you again");
        tags.add("faded");
        tags.add("篮球");
        tags.add("网球");
        tags.add("棒球");
        tags.add("乒乓球");
        tags.add("足球");
        tags.add("跑步");
        tags.add("游泳");
        tags.add("冰球");
        tags.add("高尔夫球");
        tags.add("橄榄球");
        tags.add("羽毛球");
        tags.add("桌球");
        adapter.addTags(tags);
        loadTags();


        builder.setView(view);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<Integer> list = tagLayout.getSelectedList();

                final List<String> selectedTags = new ArrayList<>();
                for (int index : list)
                    selectedTags.add(adapter.getTagWithPosition(index));
                if (selectedTags.size() < 5) {
                    Toast.makeText(getContext(), "需要選擇 5 個以上", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseDataService.addTag(FirebaseAuth.getInstance().getCurrentUser().getUid(), selectedTags).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                BasePresenter.saveUserTags(selectedTags);
                            } else {

                            }
                        }
                    });


                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });


        return builder.create();
    }

    public void loadTags() {
        List<String> userTags = BasePresenter.readUserTags();
        for (String tag : userTags) {
            adapter.setTagSelect(tag, true);
        }
        adapter.notifyDataSetChanged();

    }
}
