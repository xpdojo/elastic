# frozen_string_literal: true

SOURCE_FIELD = 'target_field'
TARGET_FIELD = 'target_info'
FIELD_NAME = 'field_name'
OPTION_NAME = 'mark_info'
OPTION_COLUMN = 'mark_type'
OPTION_NAME_COLUMN = 'mark_type_nm'
CODE_COLUMN_NAME = 'mark_code'
LANGUAGE_COLUMN_NAME = 'lang_mark'

def register(params)
  @target_field = params[SOURCE_FIELD]
  @field_name = params[FIELD_NAME]
end

def filter(event)

  event_list = []
  target_arr = event.get(@target_field)

  puts "target_arr >>>>> #{target_arr}\n\n"

  if target_arr.nil? ||
    target_arr.empty?
    return [event]
  end

  target_arr.each do |target|

    if target.nil? ||
      target[LANGUAGE_COLUMN_NAME].nil?
      next
    end

    pipe_arr = target[LANGUAGE_COLUMN_NAME].split('|')

    puts "pipe_arr >>>>> #{pipe_arr}\n\n"

    language_object = {}
    option_object = {}

    option_object[OPTION_COLUMN] = target[CODE_COLUMN_NAME]

    pipe_arr.each do |pipe|
      tmp = pipe.split(',')
      if tmp[0] == 'en'
        language_object['srch'] = tmp[1]
        language_object['en'] = tmp[1]
      else
        language_object[(tmp[0]).to_s] = tmp[1]
      end
    end
    option_object[OPTION_NAME_COLUMN] = language_object
    event_list.push(option_object)
  end

  puts "event_list >>>>> #{event_list}\n\n"

  event.set(@field_name, event_list)

  [event]
end

=begin
docker run --rm \
  -v ${PWD}/scripts:/usr/share/logstash/scripts \
  docker.elastic.co/logstash/logstash:7.17.3 \
  logstash -e "filter { ruby { path => '/usr/share/logstash/scripts/parse_language_mark.rb' } }" -t
=end
test 'with 2 airbag languages' do
  parameters do
    {
      TARGET_FIELD => OPTION_COLUMN,
      SOURCE_FIELD => TARGET_FIELD,
      FIELD_NAME => OPTION_NAME
    }
  end

  in_event do
    {
      TARGET_FIELD => [
        {
          CODE_COLUMN_NAME => 'C0440',
          LANGUAGE_COLUMN_NAME =>
            'en,Passenger Airbag|ko,Passenger Airbag'
        }
      ]
    }
  end

  expect('return 3 airbag results') do |events|
    events.first.get(OPTION_NAME).first[OPTION_NAME_COLUMN]['srch'].match(/Airbag/)
    events.first.get(OPTION_NAME).first[OPTION_NAME_COLUMN]['en'].match(/Airbag/)
  end
end

test 'with tire 2 languages' do
  parameters do
    {
      TARGET_FIELD => OPTION_COLUMN,
      SOURCE_FIELD => TARGET_FIELD,
      FIELD_NAME => OPTION_NAME
    }
  end

  in_event do
    {
      TARGET_FIELD => [
        {
          CODE_COLUMN_NAME => 'C0480',
          LANGUAGE_COLUMN_NAME =>
            'en,Spare Tire|ko,Spare Tire'
        }
      ]
    }
  end

  expect('return 3 tire results') do |events|
    events.first.get(OPTION_NAME).first[OPTION_NAME_COLUMN]['srch'].match(/Tire/)
    events.first.get(OPTION_NAME).first[OPTION_NAME_COLUMN]['en'].match(/Tire/)
  end
end
