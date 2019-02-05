// import React from "react";
// import { LikeButton } from "./likeButton";

class LikeButton extends React.Component {
    constructor(props) {
        super(props);
        this.state = {liked: false};
    }

    render() {
        return this.state.liked ? 'Hi' : (
            <button onClick={() => this.setState({liked: true})}>Like</button>
        );
    }
}

const domContainer = document.querySelector('#app');
ReactDOM.render(React.createElement(LikeButton), domContainer);
