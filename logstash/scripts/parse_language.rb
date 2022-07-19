def register(params)
  @target_field = params["target_field"]
  @field_name = params["field_name"]
end

def filter(event)

  pipeArr = event.get(@target_field).split('|')

  pipeArr.each { |element|
    tmp = element.split(',')

    if tmp.size == 0
      event.set("[#{@field_name}][srch]", nil)
      event.set("[#{@field_name}][en]", nil)
    elsif tmp.size == 1
      event.set("[#{@field_name}][srch]", tmp[0])
      event.set("[#{@field_name}][en]", tmp[0])
    elsif tmp.size > 1 and tmp[0] == 'en'
      event.set("[#{@field_name}][srch]", tmp[1])
      event.set("[#{@field_name}][en]", tmp[1])
    else
      event.set("[#{@field_name}][#{tmp[0]}]", tmp[1])
    end
  }
  return [event]
end
