public void loan() {
        // identify broke people 
        ArrayList<Player> brokePeople = new ArrayList<>();
        for (Player p : game.getPlayers()){
            if (p.getChips() == 0){
                brokePeople.add(p);
            }
        }
        if (brokePeople.isEmpty()){
            refreshUI(); // is this how to restart the round? im not sure 
            return;
        }
        // show loan shark for broke people
        for (Player broke: brokePeople){
            Stage loanStage = new Stage();
            loanStage.initModality(Modality.APPLICATION_MODAL);
            loanStage.initStyle(StageStyle.UNDECORATED);
            VBox box = new VBox(16);
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(30, 40, 30, 40));
            box.setStyle("-fx-background-color: #b43434; -fx-background-radius: 16; -fx-border-color: #6b3316; -fx-border-width: 3;");
            
            ImageView shark = new ImageView();
            URL sharkURL = getClass().getResource("/loanShark.png");
            shark.setImage(new Image(sharkURL.toExternalForm()));
            shark.setFitHeight(150);
            shark.setPreserveRatio(true);
            Label title = new Label(broke.getName() + " i see ur broke haha");
            Label message = new Label("Oh no! you don't have anymore money... do you want me to loan you some (1000) ???");
            message.setStyle("-fx-font-size: 14px; -fx-text-alignment: center;");
            message.setWrapText(true);
            message.setMaxWidth(300);

            Button confirmButton = new Button("yes");
            confirmButton.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-background-color: #46d560");
            Button denyButton = new Button("i'd rather not :C ");
            confirmButton.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-background-color: #862626");
            confirmButton.setOnAction(e -> {
                broke.addChips(1000);
                loanStage.close();
            });
            denyButton.setOnAction(e-> {
                loanStage.close();
            });
             HBox buttons = new HBox(14, confirmButton, denyButton);
            buttons.setAlignment(Pos.CENTER);
 
            box.getChildren().addAll(shark, title, message, buttons);
            Scene sc = new Scene(box, 400, 360);
            sc.setFill(Color.TRANSPARENT);
            loanStage.setScene(sc);
            loanStage.show();
        }
        
    }
