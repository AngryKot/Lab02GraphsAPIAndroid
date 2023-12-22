package practice.lab02.graphsAPI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import practice.lab02.graphsAPI.model.GraphItem;
import practice.lab02.graphsAPI.model.IdDTO;
import practice.lab02.graphsAPI.model.LinkItem;
import practice.lab02.graphsAPI.model.NodeItem;
import practice.lab02.graphsAPI.databinding.ActivityMainBinding;
import practice.lab02.graphsAPI.databinding.AddNodeDialogBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding = null; //инициализируем объект привязки C Sharp в файле build.gradleModule

    ArrayAdapter<GraphItem> graphsAdapter;
    private GraphAPI api = ApiBuilder.getAPI();
    private SharedPreferencesRepository repository;
    private MainActivity mainActivity;

    private boolean editEnable = false;
    private boolean deleteEnable = false;
    private boolean copyEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());//создание объекта привязки
        setContentView(binding.getRoot()); //стало
        repository = new SharedPreferencesRepository(this.getApplicationContext());
        mainActivity = this;
        graphsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        binding.listGraphs.setAdapter(graphsAdapter); //привязка адаптера к лист вью
        binding.listGraphs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!editEnable && !deleteEnable && !copyEnable) {
                    Intent i = new Intent(getApplicationContext(), SecondActivity.class);
                    i.putExtra("GRAPH_ID", graphsAdapter.getItem(position).id);//передача id нажатого графа в листе
                    startActivity(i); //переход на 2ю активити
                } else if (editEnable) {
                    openEditGraphDialog(graphsAdapter.getItem(position).id);
                    editEnable = false;
                } else if (copyEnable) {
//                    api.addGraph(graphsAdapter.getItem(position).name).enqueue(new Callback<GraphItem>() {
//                        @Override
//                        public void onResponse(Call<GraphItem> call, Response<GraphItem> response) {
//                            refreshGraphs();
//                            GraphItem newGraph = graphsAdapter.getItem(graphsAdapter.getCount() - 1);
//                            ArrayList<NodeItem> oldNodes = new ArrayList<>();
//                            api.getNodes(graphsAdapter.getItem(position).id).enqueue(new Callback<List<NodeItem>>() {
//                                @Override
//                                public void onResponse(Call<List<NodeItem>> call, Response<List<NodeItem>> response) {
//                                    oldNodes.addAll(response.body());
//                                }
//
//                                @Override
//                                public void onFailure(Call<List<NodeItem>> call, Throwable t) {}
//                            });
//                            //ArrayList<NodeItem> oldNodes = database.getNodesInGraph(graphsAdapter.getItem(position).id);
//                            for(NodeItem node : oldNodes) {
//                                api.addNode(node.name,newGraph.id, node.x, node.y).enqueue(new Callback<NodeItem>() {
//                                    @Override
//                                    public void onResponse(Call<NodeItem> call, Response<NodeItem> response) {
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<NodeItem> call, Throwable t) {
//                                    }
//                                });
//                                //database.addNode(newGraph.id, node.name, node.x, node.y);
//                            }
//                            ArrayList<NodeItem> newNodes = new ArrayList<>();
//                            api.getNodes(newGraph.id).enqueue(new Callback<List<NodeItem>>() {
//                                @Override
//                                public void onResponse(Call<List<NodeItem>> call, Response<List<NodeItem>> response) {
//                                    newNodes.addAll(response.body());
//                                }
//
//                                @Override
//                                public void onFailure(Call<List<NodeItem>> call, Throwable t) {}
//                            });
//                            //ArrayList<NodeItem> newNodes = database.getNodesInGraph(newGraph.id);
//                            Map<Integer, NodeItem> nodeMap = new HashMap<>();//ключ-значение старый-новый нод
//                            for(int i = 0; i < oldNodes.size(); i++) {
//                             nodeMap.put(oldNodes.get(i).id, newNodes.get(i));
//                            }
//                            ArrayList<LinkItem> oldLinks = new ArrayList<>();
//                            api.getLinks(graphsAdapter.getItem(position).id).enqueue(new Callback<List<LinkItem>>() {
//                                @Override
//                                public void onResponse(Call<List<LinkItem>> call, Response<List<LinkItem>> response) {
//                                    oldLinks.addAll(response.body());
//                                }
//
//                                @Override
//                                public void onFailure(Call<List<LinkItem>> call, Throwable t) {
//                                }
//                            });
//                            //ArrayList<LinkItem> oldLinks = database.getLinks(graphsAdapter.getItem(position).id);
//                            for(LinkItem link : oldLinks) {
//                                api.addLink(link.text, newGraph.id, nodeMap.get(link.firstNodeId).id, nodeMap.get(link.secondNodeId).id, nodeMap.get(link.firstNodeId).x, nodeMap.get(link.firstNodeId).y, nodeMap.get(link.secondNodeId).x, nodeMap.get(link.secondNodeId).y).enqueue(new Callback<LinkItem>() {
//                                    @Override
//                                    public void onResponse(Call<LinkItem> call, Response<LinkItem> response) {
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<LinkItem> call, Throwable t) {
//                                    }
//                                });
//                                //database.addLink(newGraph.id, link.text, nodeMap.get(link.firstNodeId), nodeMap.get(link.secondNodeId));
//                            }
//                            copyEnable = false;
//                        }
//
//                        @Override
//                        public void onFailure(Call<GraphItem> call, Throwable t) {
//                        }
//                    });
//                    database.addGraph(graphsAdapter.getItem(position).name);



                } else {
                    api.graphDelete(repository.getToken(), graphsAdapter.getItem(position).id).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            deleteEnable = false;
                            refreshGraphs();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {}
                    });
                }

            }
        });
        refreshGraphs();

        binding.buttonNew.setOnClickListener(v -> {
            openAddGraphDialog();
        });
        binding.buttonEdit.setOnClickListener(v -> {
            deleteEnable = false;
            editEnable = true;
            copyEnable = false;
        });
        binding.buttonDelete.setOnClickListener(v -> {
            deleteEnable = true;
            editEnable = false;
            copyEnable = false;
        });
        binding.buttonExit.setOnClickListener(v -> {
            api.sessionDelete(repository.getToken()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    mainActivity.finishAffinity();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        });
        binding.btnDeleteAcc.setOnClickListener(v -> {
            api.accountDelete(repository.getToken()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    repository.saveAccountName("");
                    repository.saveAccountSecret("");
                    repository.saveToken("");
                    Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
                    startActivity(i);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        });
    }

    private void refreshGraphs() {
        graphsAdapter.clear();
        api.graphList(repository.getToken()).enqueue(new Callback<List<GraphItem>>() {
            @Override
            public void onResponse(Call<List<GraphItem>> call, Response<List<GraphItem>> response) {
                graphsAdapter.addAll(response.body());

                binding.buttonEdit.setEnabled(!graphsAdapter.isEmpty());
                binding.buttonDelete.setEnabled(!graphsAdapter.isEmpty());
                //binding.buttonCopy.setEnabled(!graphsAdapter.isEmpty());
            }

            @Override
            public void onFailure(Call<List<GraphItem>> call, Throwable t) {

            }
        });
    }

    private void openAddGraphDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();

        View dialogView = getLayoutInflater().inflate(R.layout.add_node_dialog, null);
        AddNodeDialogBinding dialogBinding = AddNodeDialogBinding.bind(dialogView);

        dialogBinding.tvTitle.setText(R.string.add_graph);

        dialogBinding.btnAdd.setOnClickListener(view -> {
            if (!dialogBinding.etName.getText().toString().isEmpty()) {
                api.graphCreate(repository.getToken(), dialogBinding.etName.getText().toString()).enqueue(new Callback<IdDTO>() {
                    @Override
                    public void onResponse(Call<IdDTO> call, Response<IdDTO> response) {
                        refreshGraphs();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<IdDTO> call, Throwable t) {}
                });
            }
        });
        dialog.setView(dialogView);
        dialog.getWindow().setLayout(300, 300);
        dialog.show();
    }

    private void openEditGraphDialog(Integer graphId) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();

        View dialogView = getLayoutInflater().inflate(R.layout.add_node_dialog, null);
        AddNodeDialogBinding dialogBinding = AddNodeDialogBinding.bind(dialogView);

        dialogBinding.tvTitle.setText(R.string.edit_graph);

        dialogBinding.btnAdd.setOnClickListener(view -> {
            if (!dialogBinding.etName.getText().toString().isEmpty()) {
                api.graphUpdate(repository.getToken(), graphId, dialogBinding.etName.getText().toString()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        refreshGraphs();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {}
                });
            }
        });
        dialog.setView(dialogView);
        dialog.getWindow().setLayout(300, 300);
        dialog.show();
    }
}