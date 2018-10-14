import React from 'react';

class Input extends React.Component {
  state = { text: '' };

  handleChange = e => this.setState({ text: e.target.value });

  handleKeyPress = e => {
    if (e.key === 'Enter') {
      const { text } = this.state;
      if (!/\S{1,}\s{0,}-\s{0,}\S{1,}/.test(text)) return;
      this.props.add(text);
      this.setState({ text: '' });
    }
  };

  render() {
    return (
      <input
        type={'text'}
        autoFocus={true}
        placeholder={`Add a route (e.g. "Earth - Mars")`}
        value={this.state.text}
        onChange={this.handleChange}
        onKeyPress={this.handleKeyPress}
      />
    );
  }
}

export default Input;
