const Kalah = {
	initialize() {
		Board.fill();
		Turn.get();
	},

	async getWinner() {
		const url = baseUrl + "/winner";
		const options = {
			method: "GET",
			headers: {
				"Content-Type": "application/json",
				"Access-Control-Allow-Origin": "*",
			},
		};

		const response = await fetch(url, options);
		const data = await response.json();

		if (data != "UNKNOWN") {
			Modal.toggle();
			let winner;
			let color;

			if (data == "PLAYER_A") {
				winner = "Winner: PLAYER ONE";
				color = "blue";
			} else if (data == "PLAYER_B") {
				winner = "Winner: PLAYER TWO";
				color = "red";
			} else {
				winner = "DRAW";
				color = "#555";
			}
			document.getElementById("winner").innerHTML = winner;
			document.getElementById("winner").style.color = color;
			Kalah.getBothStores();
			return;
		}
	},

	async getBothStores() {
		const url = baseUrl + "/stores";
		const options = {
			method: "GET",
			headers: {
				"Content-Type": "application/json",
				"Access-Control-Allow-Origin": "*",
			},
		};

		const response = await fetch(url, options);
		const data = await response.json();

		document.getElementById("store-one").innerHTML = data[0].stones;
		document.getElementById("store-one").style.color = "blue";
		document.getElementById("store-two").innerHTML = data[1].stones;
		document.getElementById("store-two").style.color = "red";
	},

	async reset() {
		const url = baseUrl + "/reset";
		const options = {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				"Access-Control-Allow-Origin": "*",
			},
		};
		await fetch(url, options);
		Modal.toggle();
		this.initialize();
	}
};
