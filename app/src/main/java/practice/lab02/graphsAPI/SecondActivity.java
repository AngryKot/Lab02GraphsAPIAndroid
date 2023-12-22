package practice.lab02.graphsAPI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import practice.lab02.graphsAPI.model.IdDTO;
import practice.lab02.graphsAPI.model.LinkItem;
import practice.lab02.graphsAPI.model.NodeItem;
import practice.lab02.graphsAPI.databinding.ActivitySecondBinding;
import practice.lab02.graphsAPI.databinding.AddNodeDialogBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondActivity extends AppCompatActivity {
    private ActivitySecondBinding binding = null; //инициализируем объект привязки C Sharp в файле build.gradleModule
    private Integer graphId;
    private boolean isConnection = false;
    private boolean isDelete = false;
    private boolean isDeleteLink = false;
    private NodeItem firstNode = null;

    private GraphAPI api = ApiBuilder.getAPI();
    private SharedPreferencesRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());//создание объекта привязки
        setContentView(binding.getRoot()); //стало
        repository = new SharedPreferencesRepository(this.getApplicationContext());

        graphId = getIntent().getIntExtra("GRAPH_ID", 0);//получаем id графа с 1активити
        binding.ws.addListeners(
                (newNode, newLinks) -> {
                    api.nodeUpdate(repository.getToken(), newNode.id, newNode.x, newNode.y, newNode.name).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }

//                        @Override
//                        public void onResponse(Call<NodeItem> call, Response<NodeItem> response) {
////                            for (LinkItem link: newLinks) {
////                                api.lin(link.id, link.firstX, link.firstY, link.secondX, link.secondY).enqueue(new Callback<LinkItem>() {
////                                    @Override
////                                    public void onResponse(Call<LinkItem> call, Response<LinkItem> response) {
////
////                                    }
////
////                                    @Override
////                                    public void onFailure(Call<LinkItem> call, Throwable t) {
////
////                                    }
////                                });
////                                //database.updateLink(link);
////                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<NodeItem> call, Throwable t) {
//
//                        }
                    });
                    //database.updateNodePosition(newNode.id, newNode.x, newNode.y);

                },
                node -> {
                    if (isConnection) {
                        if (firstNode == null) {
                            firstNode = node;
                        } else {
                            if(!binding.ws.isLinkExists(firstNode, node)) {
                                openAddLinkDialog(firstNode, node);
                            }
                            isConnection = false;
                            firstNode = null;
                        }
                    }

                    if (isDelete) {
                        api.nodeDelete(repository.getToken(), node.id).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                refreshNodes();

                                isDelete = false;
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                        //database.deleteNode(node.id);

                    }

                    if (isDeleteLink) {
                        if (firstNode == null) {
                            firstNode = node;
                        } else {
                            if(binding.ws.isLinkExists(firstNode, node)) {
                                api.linkDelete(repository.getToken(), binding.ws.findLink(firstNode, node).id).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        isDeleteLink = false;
                                        firstNode = null;
                                        refreshNodes();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {

                                    }
                                });
                            }
                        }
                    }
                });

        refreshNodes();

        binding.btnExit.setOnClickListener( v -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        });

        binding.btnPlus.setOnClickListener(v -> {
            openAddNodeDialog();
        });
        binding.btnDelete.setOnClickListener(v -> {
            isDelete = true;
            isConnection = false;
            isDeleteLink = false;
        });
        binding.btnMulti.setOnClickListener( v -> {
            isConnection = true;
            isDelete = false;
            isDeleteLink = false;
        });
        binding.btnDeleteLink.setOnClickListener( v -> {
            isDeleteLink = true;
            isDelete = false;
            isConnection = false;
        });
    }

    private void refreshNodes() {
        ArrayList<LinkItem> links = new ArrayList<>();
        ArrayList<NodeItem> nodes = new ArrayList<>();
        api.nodeList(repository.getToken(), graphId).enqueue(new Callback<List<NodeItem>>() {
            @Override
            public void onResponse(Call<List<NodeItem>> call, Response<List<NodeItem>> response) {
                nodes.addAll(response.body());
                api.getLinks(repository.getToken(), graphId).enqueue(new Callback<List<LinkItem>>() {
                    @Override
                    public void onResponse(Call<List<LinkItem>> call, Response<List<LinkItem>> response) {
                        links.addAll(response.body());

                        binding.ws.refreshPoints(nodes, links);
                    }

                    @Override
                    public void onFailure(Call<List<LinkItem>> call, Throwable t) {}
                });
            }

            @Override
            public void onFailure(Call<List<NodeItem>> call, Throwable t) {}
        });

    }

    private void openAddNodeDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();

        View dialogView = getLayoutInflater().inflate(R.layout.add_node_dialog, null);
        AddNodeDialogBinding dialogBinding = AddNodeDialogBinding.bind(dialogView);

        dialogBinding.tvTitle.setText(R.string.add_node);

        dialogBinding.btnAdd.setOnClickListener(view -> {
            if (!dialogBinding.etName.getText().toString().isEmpty()) {
                String name = dialogBinding.etName.getText().toString();
                api.nodeCreate(repository.getToken(), graphId, 300f, 300F, name).enqueue(new Callback<IdDTO>() {
                    @Override
                    public void onResponse(Call<IdDTO> call, Response<IdDTO> response) {
                        refreshNodes();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<IdDTO> call, Throwable t) {

                    }
                });
            }
        });
        dialog.setView(dialogView);
        dialog.show();
    }

    private void openAddLinkDialog(NodeItem firstNode, NodeItem secondNode) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();

        View dialogView = getLayoutInflater().inflate(R.layout.add_node_dialog, null);
        AddNodeDialogBinding dialogBinding = AddNodeDialogBinding.bind(dialogView);

        dialogBinding.tvTitle.setText(R.string.add_link);

        dialogBinding.btnAdd.setOnClickListener(view -> {
            if (!dialogBinding.etName.getText().toString().isEmpty()) {
                Float name = Float.parseFloat(dialogBinding.etName.getText().toString());
                api.linkCreate(repository.getToken(), firstNode.id, secondNode.id, name).enqueue(new Callback<IdDTO>() {
                    @Override
                    public void onResponse(Call<IdDTO> call, Response<IdDTO> response) {
                        refreshNodes();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<IdDTO> call, Throwable t) {

                    }
                });
                //database.addLink(graphId, name, firstNode, secondNode);

            }
        });

        dialog.setView(dialogView);
        dialog.show();
    }
}