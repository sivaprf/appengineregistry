import React, { Component } from 'react'

class ItemInfo extends Component {
  state = {
    temp:  "temp"
  }





  render() {
    console.log('BookMenu-render', this.props)
    const cssImageUrl = (this.props.item.image) ? 'url(\'' + this.props.item.image + '\')' : ''
    return (
      <div className="book">
        <div className="book-top">
          <div className="book-cover" style={{ width: 128, height: 193, backgroundImage: cssImageUrl  }}></div>
        </div>
        <div className="book-title">{this.props.item.sku}</div>
        <div className="book-authors">{this.props.item.summary}</div>
      </div>
    )
  }
}

export default ItemInfo
