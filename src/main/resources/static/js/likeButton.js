export class LikeButton extends React.Component {
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
