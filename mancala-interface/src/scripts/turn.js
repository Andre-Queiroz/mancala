const Turn = {
	async get() {
		const url = baseUrl + "/turn";
		const options = {
			method: "GET",
			headers: {
				"Content-Type": "application/json",
				"Access-Control-Allow-Origin": "*",
			},
		};

		const response = await fetch(url, options);
		const data = await response.json();
		Turn.render(data);
	},

	render(data) {
		let turnMessage;
		let color;

		if (data == "PLAYER_A") {
			turnMessage = "one";
			color = "blue";
			for (let i = 0; i < 6; i++) {
				document.getElementById("player-context-one").children[i].classList.add("turn-style");
				document.getElementById("player-context-two").children[i].classList.remove("turn-style");
			}
		} else {
			turnMessage = "two";
			color = "red";
			for (let i = 0; i < 6; i++) {
				document.getElementById("player-context-two").children[i].classList.add("turn-style");
				document.getElementById("player-context-one").children[i].classList.remove("turn-style");
			}
		}
		document.getElementById("turn").innerHTML = turnMessage;
		document.getElementById("turn").style.color = color;
	},
};
